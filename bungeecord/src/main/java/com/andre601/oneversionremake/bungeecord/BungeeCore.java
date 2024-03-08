/*
 * Copyright 2020 - 2021 Andre601
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
import com.andre601.oneversionremake.core.Parser;
import com.andre601.oneversionremake.core.commands.CommandHandler;
import com.andre601.oneversionremake.core.proxy.ProtocolVersionResolver;
import com.andre601.oneversionremake.core.proxy.ProxyPlatform;
import com.andre601.oneversionremake.core.files.ConfigHandler;
import com.andre601.oneversionremake.core.interfaces.PluginCore;
import com.andre601.oneversionremake.core.interfaces.ProxyLogger;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bstats.charts.SimplePie;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class BungeeCore extends Plugin implements PluginCore{
    
    private OneVersionRemake core;
    private final ProxyLogger logger = new BungeeLogger(getLogger());
    
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
    public void loadMetrics(){
        Metrics metrics = new Metrics(this, 10340);
        
        metrics.addCustomChart(new DrilldownPie("allowed_protocols", () -> core.getPieMap()));
        metrics.addCustomChart(new SimplePie("proxy_type", () -> getProxyPlatform().getName()));
    }
    
    @Override
    public Path getPath(){
        return getDataFolder().toPath();
    }
    
    @Override
    public ProxyPlatform getProxyPlatform(){
        switch(getProxy().getName().toLowerCase(Locale.ROOT)){
            case "flamecord":
                return ProxyPlatform.FLAMECORD;
            
            case "travertine":
                return ProxyPlatform.TRAVERTINE;
            
            case "waterfall":
                return ProxyPlatform.WATERFALL;
            
            case "bungeecord":
            default:
                return ProxyPlatform.BUNGEECORD;
        }
    }
    
    @Override
    public ProxyLogger getProxyLogger(){
        return logger;
    }
    
    @Override
    public ConfigHandler getConfigHandler(){
        return core.getConfigHandler();
    }
    
    @Override
    public ProtocolVersionResolver getProtocolVersionResolver(){
        return core.getProtocolVersionResolver();
    }
    
    @Override
    public CommandHandler getCommandHandler(){
        return core.getCommandHandler();
    }
    
    @Override
    public Parser getComponentParser(){
        return core.getComponentParser();
    }
    
    @Override
    public String getVersion(){
        return core.getVersion();
    }
    
    @Override
    public String getProxyVersion(){
        String[] version = getProxy().getVersion().split(":");
        if(version.length <= 0)
            return "UNKNOWN";
        
        if(version.length == 1)
            return version[0];
        
        return String.format("%s (Build #%s)", version[2], version[4]);
    }
    
    public ServerPing.PlayerInfo[] getPlayers(List<String> lines, List<Integer> serverProtocols, int userProtocol, boolean majorOnly, boolean blacklist){
        return core.getPlayers(ServerPing.PlayerInfo.class, lines, serverProtocols, userProtocol, majorOnly, blacklist)
                   .toArray(new ServerPing.PlayerInfo[0]);
    }
    
    public OneVersionRemake getCore(){
        return core;
    }
}
