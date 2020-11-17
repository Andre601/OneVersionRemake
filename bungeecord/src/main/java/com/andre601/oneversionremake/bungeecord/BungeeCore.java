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

package com.andre601.oneversionremake.bungeecord;

import com.andre601.oneversionremake.bungeecord.commands.CmdOneVersionRemake;
import com.andre601.oneversionremake.bungeecord.listener.BungeeLoginListener;
import com.andre601.oneversionremake.bungeecord.listener.BungeePingListener;
import com.andre601.oneversionremake.bungeecord.logging.BungeeLogger;
import com.andre601.oneversionremake.core.OneVersionRemake;
import com.andre601.oneversionremake.core.enums.ProxyPlatform;
import com.andre601.oneversionremake.core.files.ConfigHandler;
import com.andre601.oneversionremake.core.interfaces.PluginCore;
import com.andre601.oneversionremake.core.interfaces.ProxyLogger;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.List;

public class BungeeCore extends Plugin implements PluginCore{
    
    private OneVersionRemake core;
    private final ProxyLogger logger = new BungeeLogger(getLogger());
    
    private ConfigHandler configHandler = null;
    
    @Override
    public void onEnable(){
        this.core = new OneVersionRemake(this);
    }
    
    // PluginCore stuff
    
    @Override
    public void loadCommands(){
        getProxy().getPluginManager().registerCommand(this, new CmdOneVersionRemake(this));
    }
    
    @Override
    public void loadEventListeners(){
        new BungeeLoginListener(this);
        new BungeePingListener(this);
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
        return getDataFolder().toPath();
    }
    
    @Override
    public ProxyPlatform getProxyPlatform(){
        try{
            Class.forName("io.github.waterfallmc.waterfall.conf.WaterfallConfiguration");
            return ProxyPlatform.WATERFALL;
        }catch(ClassNotFoundException ex){
            return ProxyPlatform.BUNGEECORD;
        }
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
    
    public TextComponent getTextComponent(List<String> lines, List<Integer> serverProtocols, int userProtocol){
        return new TextComponent(BungeeComponentSerializer.get()
                .serialize(core.getTextComponent(lines, serverProtocols, userProtocol)));
    }
    
    public String getText(String text, List<Integer> serverProtocols, int userProtocol){
        return core.getText(text, serverProtocols, userProtocol);
    }
}
