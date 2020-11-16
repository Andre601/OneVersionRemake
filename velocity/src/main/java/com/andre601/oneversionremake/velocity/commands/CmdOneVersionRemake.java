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

package com.andre601.oneversionremake.velocity.commands;

import com.andre601.oneversionremake.velocity.VelocityCore;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CmdOneVersionRemake implements SimpleCommand{
    
    private final VelocityCore plugin;
    
    public CmdOneVersionRemake(VelocityCore plugin){
        this.plugin = plugin;
    }
    
    @Override
    public void execute(Invocation invocation){
        CommandSource commandSource = invocation.source();
        String[] args = invocation.arguments();
        
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            if(!commandSource.hasPermission("oneversionremake.command.help")){
                sendMsg(commandSource, NamedTextColor.RED, "Insufficient permissions!");
                return;
            }
            
            sendMsg(commandSource, "OneVersionRemake v%s", plugin.getVersion());
            sendMsg(commandSource);
            sendMsg(commandSource, NamedTextColor.AQUA, "/ovr help");
            sendMsg(commandSource, NamedTextColor.GRAY, "Displays this Command list.");
            sendMsg(commandSource);
            sendMsg(commandSource, NamedTextColor.AQUA, "/ovr reload");
            sendMsg(commandSource, NamedTextColor.GRAY, "Reloads the configuration file.");
        }else
        if(args[0].equalsIgnoreCase("reload")){
            if(!commandSource.hasPermission("oneversionremake.command.reload")){
                sendMsg(commandSource, NamedTextColor.RED, "Insufficient permissions!");
                return;
            }
            
            if(plugin.reloadConfig()){
                sendMsg(commandSource, NamedTextColor.GREEN, "Config.yml successfully reloaded!");
            }else{
                sendMsg(commandSource, NamedTextColor.RED, "There was an error while reloading the config.");
                sendMsg(commandSource, NamedTextColor.RED, "Please check the consol for errors.");
            }
        }
    }
    
    private void sendMsg(CommandSource commandSource){
        sendMsg(commandSource, "");
    }
    
    private void sendMsg(CommandSource commandSource, String msg, Object... args){
        sendMsg(commandSource, NamedTextColor.WHITE, msg, args);
    }
    
    private void sendMsg(CommandSource commandSource, NamedTextColor color, String msg, Object... args){
        commandSource.sendMessage(Component.text(String.format(msg, args)).color(color));
    }
}
