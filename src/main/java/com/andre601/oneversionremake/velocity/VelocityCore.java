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

import com.andre601.oneversionremake.core.ConfigHandler;
import com.andre601.oneversionremake.core.OneVersionRemake;
import com.andre601.oneversionremake.core.SnakeYAML;
import com.andre601.oneversionremake.velocity.commands.CmdOneVersionRemake;
import com.andre601.oneversionremake.velocity.listener.LoginListener;
import com.andre601.oneversionremake.velocity.listener.PingListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Plugin(
        id = "oneversionremake",
        name = "OneVersionRemake"
)
public class VelocityCore implements OneVersionRemake.Core{
    private final Logger logger;
    private final ProxyServer proxy;
    private final Path pluginFolder;
    
    private OneVersionRemake core;
    
    @Inject
    public VelocityCore(Logger logger, ProxyServer proxy, @DataDirectory Path pluginFolder){
        this.logger = logger;
        this.proxy = proxy;
        this.pluginFolder = pluginFolder;
    }
    
    @Subscribe
    public void initialize(ProxyInitializeEvent event){
        try{
            this.proxy.getPluginManager().addToClasspath(this, SnakeYAML.load(this.pluginFolder));
        }catch(IOException ex){
            logger.error("Unable to load dependency SnakeYAML.", ex);
            return;
        }
        
        this.core = new OneVersionRemake(this);
    }
    
    @Override
    public void enable(){
        logger.info("Loading command...");
        new CmdOneVersionRemake(this);
        logger.info("Command loaded!");
        
        logger.info("Loading listener...");
        new LoginListener(this);
        new PingListener(this);
        logger.info("Listener loaded!");
    
        logger.info("OneVersionRemake is ready!");
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
    public OneVersionRemake.Platform getPlatform(){
        return OneVersionRemake.Platform.VELOCITY;
    }
    
    @Override
    public Logger getProxyLogger(){
        return logger;
    }
    
    public ConfigHandler getConfigHandler(){
        return null;
    }
    
    public String getVersion(){
        return core.getVersion();
    }
    
    public ProxyServer getProxy(){
        return proxy;
    }
    
    public TextComponent getComponent(List<String> lines, List<Integer> serverProtocols, int userProtocol){
        return TextComponent.of(getText(String.join("\n", lines), serverProtocols, userProtocol));
    }
    
    public String getText(String text, List<Integer> serverProtocols, int userProtocol){
        text = text.replace("{version}", OneVersionRemake.Versions.getFriendlyName(serverProtocols))
                .replace("{userVersion}", OneVersionRemake.Versions.getFriendlyName(userProtocol));
        
        return LegacyComponentSerializer.legacy().serialize(TextComponent.of(text));
    }
}
