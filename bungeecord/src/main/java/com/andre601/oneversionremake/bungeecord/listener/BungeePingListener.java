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

package com.andre601.oneversionremake.bungeecord.listener;

import com.andre601.oneversionremake.bungeecord.BungeeCore;
import com.andre601.oneversionremake.core.Parser;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BungeePingListener implements Listener{
    
    private final BungeeCore plugin;
    
    public BungeePingListener(BungeeCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing ping = event.getResponse();
        ServerPing.Protocol protocol = ping.getVersion();
        if(protocol == null)
            return;
        
        int userProtocol = protocol.getProtocol();
    
        List<Integer> serverProtocols = plugin.getConfigHandler().getIntList("Protocol", "Versions");
        List<String> hoverMessage = plugin.getConfigHandler().getStringList("Messages", "Hover");
        
        if(serverProtocols.isEmpty())
            return;
    
        serverProtocols.sort(Comparator.reverseOrder());
        
        String playerCount = plugin.getConfigHandler().getString("", "Messages", "PlayerCount");
        List<String> motd = plugin.getConfigHandler().getStringList("Messages", "Motd");
        
        if(!serverProtocols.contains(userProtocol)){
            if(!playerCount.isEmpty())
                ping.getPlayers().setSample(getSamplePlayers(hoverMessage, serverProtocols, userProtocol));
            
            if(!playerCount.isEmpty())
                protocol.setName(Parser.toString(playerCount, serverProtocols, userProtocol));
            
            if(!motd.isEmpty()){
                if(motd.size() > 2)
                    motd = motd.subList(0, 1);
                
                TextComponent component = new TextComponent(BungeeComponentSerializer.get().serialize(
                        Parser.toTextComponent(motd, serverProtocols, userProtocol)
                ));
                
                ping.setDescriptionComponent(component);
            }
            
            protocol.setProtocol(serverProtocols.get(0));
            
            ping.setVersion(protocol);
            event.setResponse(ping);
        }
    }
    
    private ServerPing.PlayerInfo[] getSamplePlayers(List<String> lines, List<Integer> serverProtocols, int userProtocol){
        ServerPing.PlayerInfo[] samplePlayers = new ServerPing.PlayerInfo[lines.size()];
        for(int i = 0; i < samplePlayers.length; i++){
            samplePlayers[i] = new ServerPing.PlayerInfo(Parser.toString(lines.get(i), serverProtocols, userProtocol), UUID.randomUUID());
        }
        
        return samplePlayers;
    }
}
