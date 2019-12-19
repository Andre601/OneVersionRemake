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
import com.andre601.oneversionremake.util.Versions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
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
    private CommandSender sender;
    
    @Override
    public void onEnable(){
        sender = getProxy().getConsole();
        sendMessage(String.format(
                "&fEnabling OneVersionRemake v%s...",
                getDescription().getVersion()
        ));
        
        sendMessage("&fAttempting to load config.yml...");
        saveDefaultConfig();
        
        try{
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch(IOException ex){
            sendMessage("&cUnable to load config! Plugin won't be enabled.");
            return;
        }
        sendMessage("&fConfig.yml loaded!");
        
        sendMessage("&fLoading Protocol version from the config...");
        
        int protocol = config.getInt("Protocol.Version", -1);
        /*
         * We check if the config option "Protocol" is -1
         * In such a case will we print this warning and return to not load the listeners, preventing possible issues.
         */
        if(protocol == -1){
            sendMessage("&c================================================================================");
            sendMessage("&cWARNING!");
            sendMessage("&cThe config option \"Version\" is set to -1!");
            sendMessage("&cThe plugin won't be fully loaded to prevent any issues.");
            sendMessage("&c");
            sendMessage("&cPlease change the Version to a supported one listed here:");
            sendMessage("&chttps://github.com/Andre601/OneVersionRemake/wiki/Supported-Protocols");
            sendMessage("&c================================================================================");
            return;
        }
        sendMessage(String.format(
                "&fLoaded protocol %d (MC %s)!",
                protocol,
                Versions.getFriendlyName(protocol)
        ));
    
        sendMessage("&fLoading listeners...");
        this.getProxy().getPluginManager().registerListener(this, new PingListener(this));
        this.getProxy().getPluginManager().registerListener(this, new LoginListener(this));
        sendMessage("&fLoaded listeners!");
    
        sendMessage("&aStartup complete! OneVersionRemake is ready to use!");
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
                sendMessage(String.format(
                        "&cCould not create config.yml! Reason: %s",
                        ex.getMessage()
                ));
            }
        }
    }
    
    private void sendMessage(String text){
        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', text)));
    }
    
    public TextComponent getText(List<String> list, int protocol){
        String text = ChatColor.translateAlternateColorCodes('&', String.join("\n", list)
                .replace("{version}", Versions.getFriendlyName(protocol)));
        
        return new TextComponent(text);
    }
}
