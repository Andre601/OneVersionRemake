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
