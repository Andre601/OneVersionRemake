/*
 * Copyright 2020 Andre601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.andre601.oneversionremake.velocity;

import com.andre601.oneversionremake.core.*;
import com.andre601.oneversionremake.velocity.commands.CmdOneVersionRemake;
import com.andre601.oneversionremake.velocity.listener.LoginListener;
import com.andre601.oneversionremake.velocity.listener.PingListener;
import com.andre601.oneversionremake.velocity.logger.VelocityLogger;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

@Plugin(
        id = "oneversionremake",
        name = "OneVersionRemake",
        authors = {"Andre_601"}
)
public class VelocityCore implements PluginCore{
    private final ProxyLogger logger;
    private final ProxyServer proxy;
    private final Path pluginFolder;
    
    private OneVersionRemake core;
    
    private ConfigHandler configHandler = null;
    
    @Inject
    public VelocityCore(ProxyServer proxy, @DataDirectory Path pluginFolder){
        this.logger = new VelocityLogger(LoggerFactory.getLogger("OneVersionRemake"));
        this.proxy = proxy;
        this.pluginFolder = pluginFolder;
    }
    
    @Subscribe
    public void initialize(ProxyInitializeEvent event){ 
        this.core = new OneVersionRemake(this);
    }
    
    @Override
    public void enable(){
        logger.info("Loading command...");
        getProxy().getCommandManager().register(new CmdOneVersionRemake(this), "oneversionremake", "ovr");
        logger.info("Command loaded!");
        
        logger.info("Loading listener...");
        new LoginListener(this);
        new PingListener(this);
        logger.info("Listener loaded!");
    
        logger.info("OneVersionRemake is ready!");
    }
    
    @Override
    public void setConfigHandler(ConfigHandler configHandler){
        this.configHandler = configHandler;
    }
    
    @Override
    public boolean reloadConfig(){
        return core.reloadConfig();
    }
    
    @Override
    public Path getPath(){
        return pluginFolder;
    }
    
    @Override
    public ProxyPlatform getProxyPlatform(){
        return ProxyPlatform.VELOCITY;
    }
    
    @Override
    public ProxyLogger getProxyLogger(){
        return logger;
    }
    
    @Override
    public ConfigHandler getConfigHandler(){
        return configHandler;
    }
    
    public String getVersion(){
        return core.getVersion();
    }
    
    public ProxyServer getProxy(){
        return proxy;
    }
    
    public TextComponent getComponent(List<String> lines, List<Integer> serverProtocols, int userProtocol){
        return getComponent(String.join("\n", lines), serverProtocols, userProtocol);
    }
    
    public TextComponent getComponent(String text, List<Integer> serverProtocols, int userProtocol){
        text = text.replace("{version}", OneVersionRemake.Versions.getFriendlyName(serverProtocols))
                .replace("{userVersion}", OneVersionRemake.Versions.getFriendlyName(userProtocol));
        
        return LegacyComponentSerializer.legacy().deserialize(text, '&');
    }
}
