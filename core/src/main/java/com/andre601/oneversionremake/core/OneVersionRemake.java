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

import com.andre601.oneversionremake.core.enums.ProtocolVersion;
import com.andre601.oneversionremake.core.files.ConfigHandler;
import com.andre601.oneversionremake.core.interfaces.PluginCore;
import com.andre601.oneversionremake.core.interfaces.ProxyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class OneVersionRemake{
    
    private final PluginCore pluginCore;
    private final ConfigHandler configHandler;
    
    private String version;
    
    public OneVersionRemake(PluginCore pluginCore){
        this.pluginCore = pluginCore;
        this.configHandler = new ConfigHandler(this, pluginCore.getPath());
        
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
    
    private void start(){
        loadVersion();
        
        ProxyLogger logger = pluginCore.getProxyLogger();
        
        printBanner(logger);
        
        if(configHandler.loadConfig()){
            logger.info("Loaded config.yml!");
        }else{
            logger.warn("Couldn't load config.yml! Check above lines for errors and warnings.");
            return;
        }
    
        List<Integer> protocols = configHandler.getIntList("Protocol", "Versions");
        boolean versionsSet;
        if(protocols.isEmpty()){
            printWarning(logger);
            
            versionsSet = false;
        }else{
            logger.info("Loaded the following Protocol Version(s):");
            logger.info(ProtocolVersion.getFriendlyNames(protocols, false));
            
            versionsSet = true;
        }
        
        logger.info("Loading command /ovr...");
        pluginCore.loadCommands();
        logger.info("Command loaded!");
        
        logger.info("Loading Event Listeners...");
        pluginCore.loadEventListeners();
        logger.info("Event Listeners loaded!");
        
        logger.info("Loading Metrics...");
        if(versionsSet){
            pluginCore.loadMetrics();
            logger.info("Metrics loaded!");
        }else{
            logger.info("No Protocol Versions set. Skipping Metrics initialization...");
        }
        
        logger.info("OneVersionRemake is ready!");
    }
    
    private void printBanner(ProxyLogger logger){
        logger.info("");
        logger.info("   ____ _    ______");
        logger.info("  / __ \\ |  / / __ \\");
        logger.info(" / / / / | / / /_/ /");
        logger.info("/ /_/ /| |/ / _, _/");
        logger.info("\\____/ |___/_/ |_|");
        logger.info("");
        logger.info("OneVersionRemake v" + getVersion());
        logger.info("Platform: " + pluginCore.getProxyPlatform().getName());
        logger.info("");
    }
    
    private void printWarning(ProxyLogger logger){
        logger.warn("================================================================================");
        logger.warn("WARNING!");
        logger.warn("The config option 'Versions' doesn't contain any protocol numbers!");
        logger.warn("Please edit the config to include valid protocol numbers or OneVersionRemake");
        logger.warn("won't work as expected.");
        logger.warn("");
        logger.warn("You may find a list of supported protocol versions here:");
        logger.warn("https://github.com/Andre601/OneVersionRemake/wiki/Supported-Protocols");
        logger.warn("");
        logger.warn("OneVersionRemake won't handle joining Players to prevent any possible issues.");
        logger.warn("================================================================================");
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
