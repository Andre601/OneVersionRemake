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
import com.andre601.oneversionremake.core.OneVersionRemake;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Collections;
import java.util.List;

public class LoginListener implements Listener{
    
    private BungeeCore core;
    
    public LoginListener(BungeeCore core){
        this.core = core;
        core.getProxy().getPluginManager().registerListener(core, this);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PreLoginEvent event){
        List<Integer> protocols = core.getConfigHandler().getIntList("Protocol", "Versions");
        List<String> message = core.getConfigHandler().getStringList("Messages", "Kick");
        
        int clientProtocol = event.getConnection().getVersion();
        if(protocols.isEmpty())
            return;
        
        if(!protocols.contains(clientProtocol)){
            if(message.isEmpty())
                message = Collections.singletonList("&cOutdated Client version! This Server is running MC {version}!");
            
            event.setCancelReason(core.getComponent(message, protocols, clientProtocol));
            event.setCancelled(true);
            
            if(core.getConfigHandler().getBoolean(true, "Protocol", "LogDenial"))
                core.getLogger().info(String.format(
                        "Denied login for Player %s with MC version %s (Protocol %d)",
                        event.getConnection().getName(),
                        OneVersionRemake.Versions.getFriendlyName(clientProtocol),
                        clientProtocol
                ));
        }
    }
}
