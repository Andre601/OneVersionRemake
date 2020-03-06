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
        int protocol = plugin.getConfig().getInt("Protocol.Version", -1);
        int clientProtocol = event.getConnection().getVersion();
        List<String> message = plugin.getConfig().getStringList("Messages.Kick");
        boolean isExact = plugin.getConfig().getBoolean("Protocol.Exact", false);
        
        if(isExact){
            if(clientProtocol != protocol){
                if(message.isEmpty())
                    message = Collections.singletonList("&cOutdated Client version! This network is on {version}.");
                
                event.setCancelReason(plugin.getTextComponent(message, protocol, clientProtocol));
                event.setCancelled(true);
            }
        }else{
            if(clientProtocol < protocol){
                if(message.isEmpty())
                    message = Collections.singletonList("&cOutdated Client version! This network is on {version}.");
    
                event.setCancelReason(plugin.getTextComponent(message, protocol, clientProtocol));
                event.setCancelled(true);
            }
        }
    }
}
