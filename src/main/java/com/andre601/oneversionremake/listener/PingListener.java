package com.andre601.oneversionremake.listener;

import com.andre601.oneversionremake.OneVersionRemake;
import com.andre601.oneversionremake.Versions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.List;
import java.util.UUID;

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
        List<String> hoverList = plugin.getConfig().getStringList("HoverMessage");
        
        
        if(protocol.getProtocol() < protocolId){
            protocol.setProtocol(protocolId);
            
            if(!hoverList.isEmpty()){
                ping.getPlayers().setSample(new ServerPing.PlayerInfo[]{
                        new ServerPing.PlayerInfo(
                                ChatColor.translateAlternateColorCodes('&', String.join("\n", hoverList))
                                .replace("{version}", Versions.getFriendlyName(protocolId)),
                                UUID.fromString("0-0-0-0-0")
                        )
                });
            }
            if(!protocolName.isEmpty())
                protocol.setName(protocolName.replace("{version}", Versions.getFriendlyName(protocolId)));
        }
        
        ping.setVersion(protocol);
        event.setResponse(ping);
    }
}
