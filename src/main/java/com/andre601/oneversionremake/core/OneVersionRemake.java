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

import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OneVersionRemake{
    
    private final Core core;
    private final ConfigHandler configHandler;
    
    private final String version = getClass().getPackage().getImplementationVersion();
    
    public OneVersionRemake(Core core){
        this.core = core;
        this.configHandler = new ConfigHandler(this, core.getPath());
        
        start();
    }
    
    public Logger getLogger(){
        return core.getProxyLogger();
    }
    
    private void start(){
        Logger logger = core.getProxyLogger();
        
        logger.info("");
        logger.info("   ____ _    ______");
        logger.info("  / __ \\ |  / / __ \\");
        logger.info(" / / / / | / / /_/ /");
        logger.info("/ /_/ /| |/ / _, _/");
        logger.info("\\____/ |___/_/ |_|");
        logger.info("");
        logger.info("OneVersionRemake v" + getVersion());
        logger.info("");
        logger.info("Platform: " + core.getPlatform().getString());
        logger.info("");
    
        if(configHandler.loadConfig()){
            logger.info("Config.yml loaded!");
        }else{
            logger.warn("Disabling plugin!");
            return;
        }
        
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
        
        logger.info("Loaded Protocol Version(s) " + Versions.getFriendlyName(protocols));
        
        core.enable();
    }
    
    public ConfigHandler getConfigHandler(){
        return configHandler;
    }
    
    public boolean reloadConfig(){
        return configHandler.reload();
    }
    
    public String getVersion(){
        return version;
    }
    
    public interface Core{
        
        void enable();
        
        boolean reloadConfig();
        
        Path getPath();
        
        Platform getPlatform();
        
        Logger getProxyLogger();
    }
    
    public enum Platform{
        BUNGEE   ("BungeeCord"),
        WATERFALL("BungeeCord - [Waterfall]"),
        VELOCITY ("Velocity");
        
        private final String platform;
        
        Platform(String platform){
            this.platform = platform;
        }
        
        public String getString(){
            return platform;
        }
    }
    
    public enum Versions{
        MC_1_16_3(753, "1.16.3"),
        MC_1_16_2(751, "1.16.2"),
        MC_1_16_1(736, "1.16.1"),
        MC_1_16  (735, "1.16"),
        MC_1_15_2(578, "1.15.2"),
        MC_1_15_1(575, "1.15.1"),
        MC_1_15  (573, "1.15"),
        MC_1_14_4(498, "1.14.4"),
        MC_1_14_3(490, "1.14.3"),
        MC_1_14_2(485, "1.14.2"),
        MC_1_14_1(480, "1.14.1"),
        MC_1_14  (477, "1.14"),
        MC_1_13_2(404, "1.13.2"),
        MC_1_13_1(401, "1.13.1"),
        MC_1_13  (393, "1.13"),
        MC_1_12_2(340, "1.12.2"),
        MC_1_12_1(338, "1.12.1"),
        MC_1_12  (335, "1.12"),
        MC_1_11_2(316, "1.11.2"),
        MC_1_11  (315, "1.11"),
        MC_1_10_2(210, "1.10.2"),
        MC_1_9_4 (110, "1.9.4"),
        MC_1_9_2 (109, "1.9.2"),
        MC_1_9_1 (108, "1.9.1"),
        MC_1_9   (107, "1.9"),
        MC_1_8_9 (47,  "1.8.9"),
        UNKNOWN  (0,   "?");
    
        private final int protocol;
        private final String name;
    
        Versions(int protocol, String name){
            this.protocol = protocol;
            this.name = name;
        }
    
        private String getName(){
            return name;
        }
    
        private int getProtocol(){
            return protocol;
        }
    
        public static String getFriendlyName(int protocol){
            for(Versions version : values()){
                if(version.getProtocol() == protocol)
                    return version.getName();
            }
            return UNKNOWN.getName();
        }
        
        public static String getFriendlyName(List<Integer> protocols){
            List<String> friendlyNames = new ArrayList<>();
            for(int i : protocols)
                friendlyNames.add(getFriendlyName(i));
            
            return String.join(", ", friendlyNames);
        }
    }
}
