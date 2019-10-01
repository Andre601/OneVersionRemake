package com.andre601.oneversionremake.listener;

import com.andre601.oneversionremake.OneVersionRemake;
import com.andre601.oneversionremake.Versions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PingListener implements Listener{
    
    private OneVersionRemake plugin;
    
    public PingListener(OneVersionRemake plugin){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPing(ProxyPingEvent event){
        ServerPing ping = event.getResponse();
        ServerPing.Protocol protocol = ping.getVersion();
        
        String protocolName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ProtocolName"));
        int protocolId = plugin.getConfig().getInt("Protocol");
        
        if(!protocolName.isEmpty())
            protocol.setName(protocolName.replace("{version}", Versions.getFriendlyName(protocolId)));
        
        if(!(protocol.getProtocol() > protocolId))
            protocol.setProtocol(protocolId);
        
        ping.setVersion(protocol);
        event.setResponse(ping);
    }
}
