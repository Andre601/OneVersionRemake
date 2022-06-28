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

package com.andre601.oneversionremake.core.files;

import com.andre601.oneversionremake.core.OneVersionRemake;
import com.andre601.oneversionremake.core.interfaces.ProxyLogger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigHandler{
    
    private final OneVersionRemake core;
    private final ProxyLogger logger;
    
    private final File config;
    private final File path;
    
    private ConfigurationNode node = null;
    
    public ConfigHandler(OneVersionRemake core, Path path){
        this.core = core;
        logger = core.getProxyLogger();
        
        this.config = new File(path.toFile(), "config.yml");
        this.path = path.toFile();
    }
    
    public boolean loadConfig(){
        if(!path.isDirectory() && !path.mkdirs()){
            logger.warn("Could not create folder for plugin!");
            return false;
        }
        
        if(!config.exists()){
            try(InputStream is = core.getClass().getResourceAsStream("/config.yml")){
                if(is == null){
                    logger.warn("Unable to create config file! InputStream was null.");
                    return false;
                }
                
                Files.copy(is, config.toPath());
            }catch(IOException ex){
                logger.warn("Unable to create config file!", ex);
                return false;
            }
        }
    
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .file(config)
                .build();
        
        try{
            node = loader.load();
        }catch(IOException ex){
            logger.warn("There was an issue while attempting to load the config.", ex);
            return false;
        }
        
        return true;
    }
    
    public boolean reload(){
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .file(config)
                .build();
        
        try{
            node = loader.load();
            return true;
        }catch(IOException ex){
            logger.warn("There was an issue while attempting to reload the config", ex);
            return false;
        }
    }
    
    public boolean getBoolean(boolean def, Object... path){
        return fromPath(path).getBoolean(def);
    }
    
    public String getString(String def, Object... path){
        return fromPath(path).getString(def);
    }
    
    public List<Integer> getIntList(Object... path){
        try{
            return fromPath(path).getList(Integer.class);
        }catch(SerializationException ex){
            return new ArrayList<>();
        }
    }
    
    public List<String> getStringList(boolean trim, Object... path){
        List<String> list;
        try{
            list = fromPath(path).getList(String.class);
        }catch(SerializationException ex){
            return Collections.emptyList();
        }
        
        if(list == null)
            return Collections.emptyList();
        
        return (trim && list.size() > 2) ? list.subList(0, 2) : list;
    }
    
    private ConfigurationNode fromPath(Object... path){
        return node.node(path);
    }
}
