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

package com.andre601.oneversionremake.core;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class OneVersionRemake{
    
    private final PluginCore core;
    private final ConfigHandler configHandler;
    
    private final String version = getClass().getPackage().getImplementationVersion();
    
    public OneVersionRemake(PluginCore core){
        this.core = core;
        this.configHandler = new ConfigHandler(this, core.getPath());
        
        start();
    }
    
    public ProxyLogger getLogger(){
        return core.getProxyLogger();
    }
    
    private void start(){
        ProxyLogger logger = core.getProxyLogger();
        
        logger.info("");
        logger.info("   ____ _    ______");
        logger.info("  / __ \\ |  / / __ \\");
        logger.info(" / / / / | / / /_/ /");
        logger.info("/ /_/ /| |/ / _, _/");
        logger.info("\\____/ |___/_/ |_|");
        logger.info("");
        logger.info("OneVersionRemake v" + getVersion());
        logger.info("");
        logger.info("Platform: " + core.getProxyPlatform().getName());
        logger.info("");
    
        if(configHandler.loadConfig()){
            logger.info("Config.yml loaded!");
        }else{
            logger.warn("Disabling plugin!");
            return;
        }
        
        core.setConfigHandler(configHandler);
        
        List<Integer> protocols = configHandler.getIntList("Protocol", "Versions");
        if(protocols.isEmpty()){
            logger.warn("================================================================================");
            logger.warn("WARNING!");
            logger.warn("The config option 'Versions' doesn't contain any numbers.");
            logger.warn("Please alter the config.yml to make the plugin work.");
            logger.warn("");
            logger.warn("A list of known and supported Protocol Numbers can be found here:");
            logger.warn("https://github.com/Andre601/OneVersionRemake/wiki/Supported-Protocols");
            logger.warn("");
            logger.warn("Disabling listeners...");
            logger.warn("================================================================================");
            return;
        }
        
        logger.info("Loaded Protocol Version(s) " + Version.getFriendlyNames(protocols));
        
        core.enable();
    }
    
    public boolean reloadConfig(){
        return configHandler.reload();
    }
    
    public String getVersion(){
        return version;
    }
}
