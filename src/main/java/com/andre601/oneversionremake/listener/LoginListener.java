package com.andre601.oneversionremake.listener;

import com.andre601.oneversionremake.OneVersionRemake;
import com.andre601.oneversionremake.Versions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginListener implements Listener{
    private OneVersionRemake plugin;
    
    public LoginListener(OneVersionRemake plugin){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PreLoginEvent event){
        int protocol = plugin.getConfig().getInt("Protocol", -1);
        String kickReason = ChatColor.translateAlternateColorCodes('&', String.join("\n", 
                plugin.getConfig().getStringList("KickMessage")));
        
        if(event.getConnection().getVersion() < protocol){
            TextComponent message = new TextComponent(kickReason.replace("{version}", Versions.getFriendlyName(protocol)));
            
            event.setCancelReason(message);
            event.setCancelled(true);
        }
    }
}
