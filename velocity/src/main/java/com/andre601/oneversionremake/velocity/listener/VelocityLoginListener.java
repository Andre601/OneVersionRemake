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

import com.andre601.oneversionremake.velocity.VelocityCore;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;

import java.util.Collections;
import java.util.List;

public class VelocityLoginListener{
    
    private final VelocityCore plugin;
    
    public VelocityLoginListener(VelocityCore plugin){
        this.plugin = plugin;
        plugin.getProxy().getEventManager().register(plugin, this);
    }
    
    @Subscribe(order = PostOrder.FIRST)
    public void onPreLogin(PreLoginEvent event){
        List<Integer> serverProtocols = plugin.getConfigHandler().getIntList("Protocol", "Versions");
        List<String> kickMessage = plugin.getConfigHandler().getStringList(false, "Messages", "Kick");
        
        boolean majorOnly = plugin.getConfigHandler().getBoolean(false, "Protocol", "MajorOnly");
        boolean blacklist = plugin.getConfigHandler().getBoolean(false, "Protocol", "Blacklist");
        
        int userProtocol = event.getConnection().getProtocolVersion().getProtocol();
        if(serverProtocols.isEmpty())
            return;
        
        if((blacklist && serverProtocols.contains(userProtocol)) || (!blacklist && !serverProtocols.contains(userProtocol))){
            if(kickMessage.isEmpty())
                kickMessage = Collections.singletonList("&cThis Server is running MC {version}! Please change your client version.");
    
            PreLoginEvent.PreLoginComponentResult result = PreLoginEvent.PreLoginComponentResult
                    .denied(plugin.getComponentParser().toComponent(kickMessage, serverProtocols, userProtocol, majorOnly, blacklist));
            
            event.setResult(result);
            
            if(plugin.getConfigHandler().getBoolean(true, "Protocol", "LogDenial")){
                plugin.getProxyLogger().infoFormat(
                        "Denied login for Player %s with MC version %s (Protocol version: %d)",
                        event.getUsername(),
                        plugin.getProtocolVersionResolver().getVersions().getFriendlyName(userProtocol),
                        userProtocol
                );
            }
        }
    }
}
