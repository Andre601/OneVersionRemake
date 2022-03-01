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

package com.andre601.oneversionremake.bungeecord.commands;

import com.andre601.oneversionremake.core.CommandPermissions;
import com.andre601.oneversionremake.core.OneVersionRemake;
import com.andre601.oneversionremake.core.interfaces.CmdSender;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.CommandSender;

public class BungeeSender implements CmdSender{
    
    private final OneVersionRemake core;
    private final CommandSender sender;
    private final BungeeAudiences bungeeAudiences;
    
    public BungeeSender(OneVersionRemake core, CommandSender sender, BungeeAudiences bungeeAudiences){
        this.core = core;
        this.sender = sender;
        this.bungeeAudiences = bungeeAudiences;
    }
    
    @Override
    public boolean hasPermission(String permission){
        return sender.hasPermission(permission) || sender.hasPermission(CommandPermissions.ADMIN);
    }
    
    @Override
    public void sendMsg(){
        sendMsg("");
    }
    
    @Override
    public void sendMsg(String msg, Object... args){
        sendMsg(NamedTextColor.WHITE, msg, args);
    }
    
    @Override
    public void sendMsg(NamedTextColor color, String msg, Object... args){
        bungeeAudiences.sender(sender).sendMessage(
                core.getComponentParser().toComponent(String.format(msg, args)).color(color)
        );
    }
}
