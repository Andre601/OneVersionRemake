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
import com.velocitypowered.api.event.connection.PreLoginEvent;

import java.util.Collections;
import java.util.List;

public class LoginListener{
    
    private final VelocityCore core;
    
    public LoginListener(VelocityCore core){
        this.core = core;
        core.getProxy().getEventManager().register(core, this);
    }
    
    @Subscribe(order = PostOrder.FIRST)
    public void onLogin(PreLoginEvent event){
        List<Integer> protocols = core.getConfigHandler().getIntList("Protocol", "Versions");
        List<String> message = core.getConfigHandler().getStringList("Messages", "Kick");
        
        int clientProtocol = event.getConnection().getProtocolVersion().getProtocol();
        if(protocols.isEmpty())
            return;
        
        if(!protocols.contains(clientProtocol)){
            if(message.isEmpty())
                message = Collections.singletonList("Outdated Client version! This Server is running MC {version}!");
    
            PreLoginEvent.PreLoginComponentResult result = PreLoginEvent.PreLoginComponentResult
                    .denied(core.getComponent(message, protocols, clientProtocol));
            
            event.setResult(result);
        }
    }
}
