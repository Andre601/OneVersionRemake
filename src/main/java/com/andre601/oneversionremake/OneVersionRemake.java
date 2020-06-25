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
        info("Enabling OneVersionRemake v%s...", getDescription().getVersion());
        
        info("Loading config.yml...");
        saveDefaultConfig();
        
        try{
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch(IOException ex){
            error("§cCouldn't load config.yml (%s)! Plugin won't be enabled.", ex.getMessage());
            return;
        }
        info("Loaded config.yml successfully!");
        
        info("Loading Protocol Version from config...");
        
        int protocol = config.getInt("Protocol.Version", -1);
        /*
         * We check if the config option "Protocol" is -1
         * In such a case will we print this warning and return to not load the listeners, preventing possible issues.
         */
        if(protocol < 47){
            error("================================================================================");
            error("WARNING!");
            error("The config option \"Version\" is set to less than 47 (MC 1.8.9)!");
            error("The plugin won't be fully loaded to prevent any issues.");
            error("");
            error("Please change the Version to a supported one listed here:");
            error("https://github.com/Andre601/OneVersionRemake/wiki/Supported-Protocols");
            error("================================================================================");
            return;
        }
        info("Loaded Protocol %d (MC %s)!", protocol, Versions.getFriendlyName(protocol));
    
        info("Loading listeners...");
        new PingListener(this);
        new LoginListener(this);
        info("Loaded listeners!");
    
        info("§aStartup complete! OneVersionRemake is ready to use!");
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
                error("§cCouldn't create config.yml! Reason: %s", ex.getMessage());
            }
        }
    }
    
    public void info(String text, Object... args){
        sendMessage("§7[§fOneVersionRemake§7] " + text, args);
    }
    
    private void error(String text, Object... args){
        sendMessage("§7[§cOneVersionRemake§7] §c" + text, args);
    }
    
    private void sendMessage(String text, Object... args){
        
        sender.sendMessage(new TextComponent(String.format(
                text, 
                args
        )));
    }
    
    public TextComponent getTextComponent(List<String> list, int serverProtocol, int userProtocol){
        String text = getText(list, serverProtocol, userProtocol);
        
        return new TextComponent(text);
    }
    
    public String getText(List<String> list, int serverProtocol, int userProtocol){
        return getText(String.join("\n", list), serverProtocol, userProtocol);
    }
    
    public String getText(String text, int serverProtocol, int userProtocol){
        return ChatColor.translateAlternateColorCodes('&', text
                .replace("{version}", Versions.getFriendlyName(serverProtocol))
                .replace("{userVersion}", Versions.getFriendlyName(userProtocol))
        );
    }
}
