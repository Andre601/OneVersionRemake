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

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler{
    
    private final OneVersionRemake core;
    private final File config;
    private final File path;
    
    private ConfigurationNode node = null;
    
    public ConfigHandler(OneVersionRemake core, Path path){
        this.core = core;
        this.config = new File(path.toString(), "config.yml");
        this.path = new File(path.toString());
    }
    
    public boolean loadConfig(){
        if(!path.isDirectory() && !path.mkdirs()){
            core.getLogger().warn("Unable to create folder!");
            return false;
        }
        
        if(!config.exists()){
            try(InputStream is = core.getClass().getResourceAsStream("/config.yml")){
                Files.copy(is, config.toPath());
            }catch(IOException ex){
                core.getLogger().warn("Unable to create config file!");
                return false;
            }
        }
    
        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder()
                .setFile(config)
                .build();
        
        try{
            node = loader.load();
        }catch(IOException ex){
            core.getLogger().warn("Issue while loading the configuration file!", ex);
            return false;
        }
        
        return true;
    }
    
    public boolean reload(){
        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder()
                .setFile(config)
                .build();
        
        try{
            node = loader.load();
            return true;
        }catch(IOException ex){
            core.getLogger().warn("Error while reloading the config!", ex);
            return false;
        }
    }
    
    public ConfigurationNode getNode(){
        return node;
    }
    
    public boolean getBoolean(boolean def, Object... path){
        ConfigurationNode node = fromPath(path);
        
        return node.getBoolean(def);
    }
    
    public String getString(String def, Object... path){
        ConfigurationNode node = fromPath(path);
        
        return node.getString(def);
    }
    
    public List<Integer> getIntList(Object... path){
        ConfigurationNode node = fromPath(path);
        
        try{
            return node.getList(TypeToken.of(Integer.class));
        }catch(ObjectMappingException ex){
            return new ArrayList<>();
        }
    }
    
    public List<String> getStringList(Object... path){
        ConfigurationNode node = fromPath(path);
        
        try{
            return node.getList(TypeToken.of(String.class));
        }catch(ObjectMappingException ex){
            return new ArrayList<>();
        }
    }
    
    private ConfigurationNode fromPath(Object... path){
        return getNode().getNode(path);
    }
}
