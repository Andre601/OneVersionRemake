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
import com.andre601.oneversionremake.core.proxy.VersionsFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class OneVersionRemake{
    
    public static final String DEF_VERSIONS_URL = "https://www.andre601.ch/oneversionremake/protocol_versions.json";
    
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
        
        load();
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
            String unknown = getProtocolVersionResolver().getVersions().getFriendlyName(0);
            
            Map<String, Integer> entry = new HashMap<>();
            
            entry.put(unknown, 1);
            map.put("other", entry);
            
            return map;
        }
        
        for(int version : versions){
            String major = getProtocolVersionResolver().getVersions().getMajor(version);
            String name = getProtocolVersionResolver().getVersions().getFriendlyName(version);
            
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
    
    public <T> List<T> getPlayers(Class<T> clazz, List<String> lines, List<Integer> serverProtocols, int userProtocol, boolean majorOnly, boolean blacklist){
        try{
            final List<T> players = new ArrayList<>(lines.size());
            final Constructor<T> constructor = clazz.getDeclaredConstructor(String.class, UUID.class);
            constructor.setAccessible(true);
    
            for(String line : lines){
                players.add(constructor.newInstance(
                        getComponentParser().toString(line, serverProtocols, userProtocol, majorOnly, blacklist), UUID.randomUUID()
                ));
            }
            
            return players;
        }catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex){
            return null;
        }
    }
    
    private void load(){
        loadVersion();
        printBanner();
        
        if(getConfigHandler().loadConfig()){
            getProxyLogger().info("Loaded config.yml!");
        }else{
            getProxyLogger().warn("Couldn't load config.yml! Check above lines for errors and warnings.");
            return;
        }
        
        String url = getConfigHandler().getString(DEF_VERSIONS_URL, "Settings", "VersionsUrl");
        
        if(getProtocolVersionResolver().isFileMissing()){
            getProxyLogger().info("Creating versions.json...");
            getProtocolVersionResolver().createFile(url).whenComplete((versions, throwable) -> {
                if(handleErrors(versions, throwable))
                    return;
                
                getProxyLogger().info("Successfully created versions.json!");
                enable();
            });
        }else
        if(getConfigHandler().getBoolean(true, "Settings", "UpdateVersions")){
            getProxyLogger().info("Checking for updated versions.json...");
            getProtocolVersionResolver().updateFile(url).whenComplete((versions, throwable) -> {
                if(handleErrors(versions, throwable))
                    return;
                
                getProxyLogger().info("Update-check complete!");
                enable();
            });
        }else{
            getProxyLogger().info("Loading versions.json...");
            getProtocolVersionResolver().loadFile().whenComplete(((versions, throwable) -> {
                if(handleErrors(versions, throwable))
                    return;
                
                getProxyLogger().info("Loaded versions.json!");
                enable();
            }));
        }
    }
    
    private void enable(){
        List<Integer> protocols = configHandler.getIntList("Protocol", "Versions");
        boolean versionsSet;
        if(protocols.isEmpty()){
            printWarning();
        
            versionsSet = false;
        }else{
            getProxyLogger().info("Loaded the following Protocol Version(s):");
            getProxyLogger().info(getProtocolVersionResolver().getVersions().getFriendlyNames(protocols, false, false));
        
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
    
        getProxyLogger().info("OneVersionRemake ready!");
    }
    
    private void printBanner(){
        getProxyLogger().info("");
        getProxyLogger().info("   ____ _    ______");
        getProxyLogger().info("  / __ \\ |  / / __ \\");
        getProxyLogger().info(" / / / / | / / /_/ /");
        getProxyLogger().info("/ /_/ /| |/ / _, _/");
        getProxyLogger().info("\\____/ |___/_/ |_|");
        getProxyLogger().info("");
        getProxyLogger().infoFormat("OneVersionRemake v%s", getVersion());
        getProxyLogger().infoFormat("Platform: %s v%s", pluginCore.getProxyPlatform().getName(), pluginCore.getProxyVersion());
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
    
    private boolean handleErrors(VersionsFile versionsFile, Throwable throwable){
        if(throwable != null || versionsFile == null){
            if(throwable != null){
                getProxyLogger().warn("Encountered an Exception while handling versions.json");
                getProxyLogger().warnFormat("Error Message: %s", throwable.getMessage());
            }else{
                getProxyLogger().warn("Handling of versions.json was non-successful!");
                getProxyLogger().warn("Please check previous entries for any errors.");
            }
            return true;
        }
        
        return false;
    }
}
