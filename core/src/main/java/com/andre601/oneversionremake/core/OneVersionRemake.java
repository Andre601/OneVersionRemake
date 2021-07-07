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

package com.andre601.oneversionremake.core;

import com.andre601.oneversionremake.core.commands.CommandHandler;
import com.andre601.oneversionremake.core.files.ConfigHandler;
import com.andre601.oneversionremake.core.interfaces.PluginCore;
import com.andre601.oneversionremake.core.interfaces.ProxyLogger;
import com.andre601.oneversionremake.core.proxy.ProtocolVersionResolver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class OneVersionRemake{
    
    private final PluginCore pluginCore;
    private final ConfigHandler configHandler;
    private final CommandHandler commandHandler;
    private final ProtocolVersionResolver protocolVersionResolver;
    private final Parser parser;
    
    private String version;
    
    public OneVersionRemake(PluginCore pluginCore){
        this.pluginCore = pluginCore;
        this.configHandler = new ConfigHandler(this, pluginCore.getPath());
        this.commandHandler = new CommandHandler(this);
        this.protocolVersionResolver = new ProtocolVersionResolver(this, pluginCore.getPath());
        this.parser = new Parser(this);
        
        start();
    }
    
    public ProxyLogger getProxyLogger(){
        return pluginCore.getProxyLogger();
    }
    
    public String getVersion(){
        return version;
    }
    
    public ConfigHandler getConfigHandler(){
        return configHandler;
    }
    
    public boolean reloadConfig(){
        return configHandler.reload();
    }
    
    public CommandHandler getCommandHandler(){
        return commandHandler;
    }
    
    public ProtocolVersionResolver getProtocolVersionResolver(){
        return protocolVersionResolver;
    }
    
    public Parser getComponentParser(){
        return parser;
    }
    
    public Map<String, Map<String, Integer>> getPieMap(){
        Map<String, Map<String, Integer>> map = new HashMap<>();
        
        List<Integer> versions = getConfigHandler().getIntList("Protocol", "Versions");
        if(versions.isEmpty()){
            String unknown = getProtocolVersionResolver().getFriendlyName(0);
            
            Map<String, Integer> entry = new HashMap<>();
            
            entry.put(unknown, 1);
            map.put("other", entry);
            
            return map;
        }
        
        for(int version : versions){
            String major = getProtocolVersionResolver().getMajor(version);
            String name = getProtocolVersionResolver().getFriendlyName(version);
            
            Map<String, Integer> entry = new HashMap<>();
            entry.put(name, 1);
            
            if(major.equalsIgnoreCase("?")){
                map.put("other", entry);
            }else{
                map.put(major, entry);
            }
        }
        
        return map;
    }
    
    public <T> List<T> getPlayers(Class<T> clazz, List<String> lines, List<Integer> serverProtocols, int userProtocol, boolean majorOnly){
        try{
            final List<T> players = new ArrayList<>(lines.size());
            final Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, UUID.class);
            constructor.setAccessible(true);
    
            for(String line : lines){
                players.add(constructor.newInstance(
                        getComponentParser().toString(line, serverProtocols, userProtocol, majorOnly), UUID.randomUUID()
                ));
            }
            
            return players;
        }catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            return null;
        }
    }
    
    private void start(){
        loadVersion();
        printBanner();
        
        if(configHandler.loadConfig()){
            getProxyLogger().info("Loaded config.yml!");
        }else{
            getProxyLogger().warn("Couldn't load config.yml! Check above lines for errors and warnings.");
            return;
        }
        
        if(configHandler.getBoolean(true, "Settings", "UpdateVersions")){
            getProxyLogger().info("Fetsching latest versions.json from GitHub...");
            if(protocolVersionResolver.loadFile()){
                getProxyLogger().info("Loaded versions.json!");
            }else{
                getProxyLogger().warn("Couldn't load versions.json! Check previous lines for any errors and warnings.");
                return;
            }
        }
    
        List<Integer> protocols = configHandler.getIntList("Protocol", "Versions");
        boolean versionsSet;
        if(protocols.isEmpty()){
            printWarning();
            
            versionsSet = false;
        }else{
            getProxyLogger().info("Loaded the following Protocol Version(s):");
            getProxyLogger().info(getProtocolVersionResolver().getFriendlyNames(protocols, false));
            
            versionsSet = true;
        }
    
        getProxyLogger().info("Loading command /ovr...");
        pluginCore.loadCommands();
        getProxyLogger().info("Command loaded!");
    
        getProxyLogger().info("Loading Event Listeners...");
        pluginCore.loadEventListeners();
        getProxyLogger().info("Event Listeners loaded!");
    
        getProxyLogger().info("Loading Metrics...");
        if(versionsSet){
            pluginCore.loadMetrics();
            getProxyLogger().info("Metrics loaded!");
        }else{
            getProxyLogger().info("No Protocol Versions set. Skipping Metrics initialization...");
        }
    
        getProxyLogger().info("OneVersionRemake is ready!");
    }
    
    private void printBanner(){
        getProxyLogger().info("");
        getProxyLogger().info("   ____ _    ______");
        getProxyLogger().info("  / __ \\ |  / / __ \\");
        getProxyLogger().info(" / / / / | / / /_/ /");
        getProxyLogger().info("/ /_/ /| |/ / _, _/");
        getProxyLogger().info("\\____/ |___/_/ |_|");
        getProxyLogger().info("");
        getProxyLogger().info("OneVersionRemake v" + getVersion());
        getProxyLogger().info("Platform: " + pluginCore.getProxyPlatform().getName() + " v" + pluginCore.getProxyVersion());
        getProxyLogger().info("");
    }
    
    private void printWarning(){
        getProxyLogger().warn("================================================================================");
        getProxyLogger().warn("WARNING!");
        getProxyLogger().warn("The config option 'Versions' doesn't contain any protocol numbers!");
        getProxyLogger().warn("Please edit the config to include valid protocol numbers or OneVersionRemake");
        getProxyLogger().warn("won't work as expected.");
        getProxyLogger().warn("");
        getProxyLogger().warn("You may find a list of supported protocol versions here:");
        getProxyLogger().warn("https://github.com/Andre601/OneVersionRemake/wiki/Supported-Protocols");
        getProxyLogger().warn("");
        getProxyLogger().warn("OneVersionRemake won't handle joining Players to prevent any possible issues.");
        getProxyLogger().warn("================================================================================");
    }
    
    private void loadVersion(){
        try(InputStream is = getClass().getResourceAsStream("/core.properties")){
            Properties properties = new Properties();
            
            properties.load(is);
            
            version = properties.getProperty("version");
        }catch(IOException ex){
            version = "UNKNOWN";
        }
    }
}
