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

package com.andre601.oneversionremake.bungee.listener;

import com.andre601.oneversionremake.bungee.BungeeCore;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Collections;
import java.util.List;

public class PingListener implements Listener{
    
    private BungeeCore core;
    
    public PingListener(BungeeCore core){
        this.core = core;
        core.getProxy().getPluginManager().registerListener(core, this);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing ping = event.getResponse();
        ServerPing.Protocol protocol = ping.getVersion();
        if(protocol == null)
            return;
    
        List<Integer> protocols = core.getConfigHandler().getIntList("Protocol", "Versions");
        List<String> message = core.getConfigHandler().getStringList("Messages", "Hover");
    
        Collections.sort(protocols);
        
        String protocolName = core.getConfigHandler().getString("", "Messages", "PlayerCount");
        if(protocols.isEmpty())
            return;
        
        if(!protocols.contains(protocol.getProtocol())){
            if(!message.isEmpty())
                ping.getPlayers().setSample(getSample(message, protocols, protocol.getProtocol()));
            
            if(!protocolName.isEmpty())
                protocol.setName(core.getText(protocolName, protocols, protocol.getProtocol()));
            
            protocol.setProtocol(protocols.get(0));
            
            ping.setVersion(protocol);
            event.setResponse(ping);
        }
        
    }
    
    private ServerPing.PlayerInfo[] getSample(List<String> lines, List<Integer> serverProtocols, int userProtocol){
        ServerPing.PlayerInfo[] sample = new ServerPing.PlayerInfo[lines.size()];
        for(int i = 0; i < sample.length; i++)
            sample[i] = new ServerPing.PlayerInfo(core.getText(lines.get(i), serverProtocols, userProtocol), "0-0-0-0-0");
        
        return sample;
    }
}
