/*
 * Copyright 2019 Andre601
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

package com.andre601.oneversionremake;

import com.andre601.oneversionremake.listener.LoginListener;
import com.andre601.oneversionremake.listener.PingListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class OneVersionRemake extends Plugin{
    private File file = null;
    private Configuration config;
    
    @Override
    public void onEnable(){
        getLogger().info("Enabling OneVersionRemake v" + this.getDescription().getVersion());
        getLogger().info("Attempting to load Config.yml...");
        saveDefaultConfig();
        
        try{
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch(IOException ex){
            getLogger().severe("Unable to load config! Listeners won't be loaded.");
            return;
        }
        getLogger().info("Config.yml loaded!");
        
        getLogger().info("Loading Protocol version...");
        /*
         * We check if the config option "Protocol" is -1
         * In such a case will we print this warning and return to not load the listeners, preventing possible issues.
         */
        if(config.getInt("Protocol", -1) == -1){
            getLogger().warning("================================================================================");
            getLogger().warning("WARNING!");
            getLogger().warning("The config option \"Protocol\" is set to -1!");
            getLogger().warning("Because of that will OneVersionRemake not work properly.");
            getLogger().warning("Listeners WON'T be loaded because of this!");
            getLogger().warning("");
            getLogger().warning("Please change the Protocol version to a supported one:");
            getLogger().warning("https://wiki.vg/Protocol_version_numbers#Versions_after_the_Netty_rewrite");
            getLogger().warning("================================================================================");
            return;
        }
        getLogger().info("Loaded protocol " + config.getInt("Protocol") + " (MC " + 
                Versions.getFriendlyName(config.getInt("Protocol")) + ")!");
        
        getLogger().info("Loading listeners...");
        this.getProxy().getPluginManager().registerListener(this, new PingListener(this));
        this.getProxy().getPluginManager().registerListener(this, new LoginListener(this));
        getLogger().info("Loaded listeners!");
        getLogger().info("Plugin OneVersionRemake is ready to use!");
    }
    
    public Configuration getConfig(){
        return config;
    }
    
    private void saveDefaultConfig(){
        if(!getDataFolder().exists())
            //noinspection ResultOfMethodCallIgnored
            getDataFolder().mkdir();
        
        file = new File(getDataFolder(), "config.yml");
        
        if(!file.exists()){
            try(InputStream is = getResourceAsStream("config.yml")){
                Files.copy(is, file.toPath());
            }catch(IOException ex){
                getLogger().warning("Couldn't create config.yml! Reason: " + ex.getMessage());
            }
        }
    }
    
    public TextComponent getText(List<String> list, int protocol){
        String text = ChatColor.translateAlternateColorCodes('&', String.join("\n", list)
                .replace("{version}", Versions.getFriendlyName(protocol)));
        
        return new TextComponent(text);
    }
}
