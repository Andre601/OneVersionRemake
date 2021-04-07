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

package com.andre601.oneversionremake.velocity.listener;

import com.andre601.oneversionremake.core.Parser;
import com.andre601.oneversionremake.velocity.VelocityCore;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class VelocityPingListener{
    
    private final VelocityCore plugin;
    
    public VelocityPingListener(VelocityCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getEventManager().register(plugin, this);
    }
    
    @Subscribe(order = PostOrder.FIRST)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing ping = event.getPing();
        ServerPing.Version protocolVersion = ping.getVersion();
        if(protocolVersion == null)
            return;
        
        int userProtocol = protocolVersion.getProtocol();
    
        List<Integer> serverProtocols = plugin.getConfigHandler().getIntList("Protocol", "Versions");
        List<String> hoverMessage = plugin.getConfigHandler().getStringList("Messages", "Hover");
        
        if(serverProtocols.isEmpty())
            return;
        
        serverProtocols.sort(Comparator.reverseOrder());
    
        boolean majorOnly = plugin.getConfigHandler().getBoolean(false, "Protocol", "MajorOnly");
        
        String playerCount = plugin.getConfigHandler().getString("", "Messages", "PlayerCount");
        List<String> motd = plugin.getConfigHandler().getStringList("Messages", "Motd");
        
        if(!serverProtocols.contains(userProtocol)){
            ServerPing.Builder builder = ping.asBuilder();
            
            if(!hoverMessage.isEmpty())
                builder.samplePlayers(getSamplePlayers(hoverMessage, serverProtocols, userProtocol, majorOnly));
            
            if(!playerCount.isEmpty()){
                playerCount = Parser.toString(playerCount, serverProtocols, userProtocol, majorOnly);
                
                builder.version(new ServerPing.Version(serverProtocols.get(0), playerCount));
            }
            
            if(!motd.isEmpty()){
                if(motd.size() > 2)
                    motd = motd.subList(0, 1);
                
                builder.description(Parser.toTextComponent(motd, serverProtocols, userProtocol, majorOnly));
            }
            
            event.setPing(builder.build());
        }
    }
    
    private ServerPing.SamplePlayer[] getSamplePlayers(List<String> lines, List<Integer> serverProtocols, int userProtocol, boolean majorOnly){
        ServerPing.SamplePlayer[] samplePlayers = new ServerPing.SamplePlayer[lines.size()];
        for(int i = 0; i < samplePlayers.length; i++){
            samplePlayers[i] = new ServerPing.SamplePlayer(Parser.toString(lines.get(i), serverProtocols, userProtocol, majorOnly), UUID.randomUUID());
        }
        
        return samplePlayers;
    }
}
