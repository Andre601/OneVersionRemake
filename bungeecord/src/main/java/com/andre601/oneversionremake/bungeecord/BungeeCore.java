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

import com.andre601.oneversionremake.bungeecord.commands.BungeeSender;
import com.andre601.oneversionremake.bungeecord.commands.CmdOneVersionRemake;
import com.andre601.oneversionremake.bungeecord.listener.BungeeLoginListener;
import com.andre601.oneversionremake.bungeecord.listener.BungeePingListener;
import com.andre601.oneversionremake.bungeecord.logging.BungeeLogger;
import com.andre601.oneversionremake.core.OneVersionRemake;
import com.andre601.oneversionremake.core.commands.CommandHandler;
import com.andre601.oneversionremake.core.enums.ProtocolVersion;
import com.andre601.oneversionremake.core.enums.ProxyPlatform;
import com.andre601.oneversionremake.core.files.ConfigHandler;
import com.andre601.oneversionremake.core.interfaces.PluginCore;
import com.andre601.oneversionremake.core.interfaces.ProxyLogger;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.DrilldownPie;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BungeeCore extends Plugin implements PluginCore{
    
    private OneVersionRemake core;
    private final ProxyLogger logger = new BungeeLogger(getLogger());
    private final BungeeSender sender = new BungeeSender(this.getProxy().getConsole());
    
    @Override
    public void onEnable(){
        BungeeAudiences.create(this);
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
        
        metrics.addCustomChart(new DrilldownPie("allowed_versions", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
    
            List<Integer> protocolVersions = getConfigHandler().getIntList("Protocol", "Versions");
            if(protocolVersions.isEmpty()){
                String unknown = ProtocolVersion.getFriendlyName(0);
    
                Map<String, Integer> entry = new HashMap<>();
                
                entry.put(unknown, 1);
                map.put("other", entry);
        
                return map;
            }
    
            for(int version : protocolVersions){
                String major = ProtocolVersion.getMajor(version);
                String name = ProtocolVersion.getFriendlyName(version);
    
                Map<String, Integer> entry = new HashMap<>();
        
                entry.put(name, 1);
                if(major.equalsIgnoreCase("?")){
                    map.put("other", entry);
                }else{
                    map.put(major, entry);
                }
            }
    
            return map;
        }));
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
        return core.getConfigHandler();
    }
    
    @Override
    public CommandHandler getCommandHandler(){
        return core.getCommandHandler();
    }
    
    @Override
    public String getVersion(){
        return core.getVersion();
    }
    
    public BungeeSender getSender(){
        return sender;
    }
}
