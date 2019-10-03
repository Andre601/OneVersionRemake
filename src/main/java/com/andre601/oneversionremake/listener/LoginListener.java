package com.andre601.oneversionremake.listener;

import com.andre601.oneversionremake.OneVersionRemake;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Collections;
import java.util.List;

public class LoginListener implements Listener{
    private OneVersionRemake plugin;
    
    public LoginListener(OneVersionRemake plugin){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PreLoginEvent event){
        int protocol = plugin.getConfig().getInt("Protocol", -1);
        List<String> message = plugin.getConfig().getStringList("KickMessage");
        
        if(event.getConnection().getVersion() < protocol){
            if(message.isEmpty())
                message = Collections.singletonList("&cOutdated Client version! The Server is on {version}.");
            
            event.setCancelReason(plugin.getText(message, protocol));
            event.setCancelled(true);
        }
    }
}
