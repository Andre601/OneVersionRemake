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

package com.andre601.oneversionremake.velocity.listener;

import com.andre601.oneversionremake.velocity.VelocityCore;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PingListener{
    
    private final VelocityCore core;
    
    public PingListener(VelocityCore core){
        this.core = core;
        core.getProxy().getEventManager().register(core, this);
    }
    
    @Subscribe(order = PostOrder.FIRST)
    public void onProxyPing(ProxyPingEvent event){
        ServerPing ping = event.getPing();
        ServerPing.Version protocol = ping.getVersion();
    
        List<Integer> protocols = core.getConfigHandler().getIntList("Protocol", "Versions");
        List<String> message = core.getConfigHandler().getStringList("Messages", "Hover");
    
        Collections.sort(protocols);
        
        String protocolName = core.getConfigHandler().getString("", "Messages", "PlayerCount");
        if(protocols.isEmpty())
            return;
        
        if(!protocols.contains(protocol.getProtocol())){
            ServerPing.Builder builder = ServerPing.builder();
            
            if(!message.isEmpty())
                builder.samplePlayers(getSample(message, protocols, protocol.getProtocol()));
            
            if(!protocolName.isEmpty())
                builder.version(new ServerPing.Version(protocols.get(0), core.getText(protocolName, protocols, protocol.getProtocol())));
            
            builder.description(ping.getDescription());
            
            event.setPing(builder.build());
        }
    }
    
    private ServerPing.SamplePlayer[] getSample(List<String> lines, List<Integer> serverProtocols, int userProtocol){
        ServerPing.SamplePlayer[] sample = new ServerPing.SamplePlayer[lines.size()];
        for(int i = 0; i < sample.length; i++)
            sample[i] = new ServerPing.SamplePlayer(core.getText(lines.get(i), serverProtocols, userProtocol), UUID.fromString("0-0-0-0-0"));
        
        return sample;
    }
}
