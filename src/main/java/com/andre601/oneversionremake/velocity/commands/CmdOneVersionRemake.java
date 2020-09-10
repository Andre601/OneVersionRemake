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
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CmdOneVersionRemake implements Command{
    
    private final VelocityCore core;
    
    public CmdOneVersionRemake(VelocityCore core){
        this.core = core;
    }
    
    @Override
    public void execute(CommandSource commandSource, @NonNull String[] args){
        if(commandSource instanceof Player){
            Player player = (Player)commandSource;
            
            if(args.length == 0 || args[0].equalsIgnoreCase("help")){
                if(!player.hasPermission("oneversionremake.command.help")){
                    send(player, TextColor.RED, "Insufficient Permissions!");
                    return;
                }
                
                send(player, "OneVersionRemake v%s", core.getVersion());
                send(player, "");
                send(player, TextColor.AQUA, "/ovr help");
                send(player, TextColor.GRAY, "Shows this help.");
                send(player, TextColor.AQUA, "/ovr reload");
                send(player, TextColor.GRAY, "Reloads the configuration file");
            }else
            if(args[0].equalsIgnoreCase("reload")){
                if(!player.hasPermission("oneversionremake.command.reload")){
                    send(player, TextColor.RED, "Insufficient Permissions!");
                    return;
                }
                
                if(core.reloadConfig())
                    send(player, TextColor.GREEN, "Successfully reloaded config.yml!");
                else
                    send(player, TextColor.RED, "Error while reloading config. Please check the console!");
            }
        }else{
            if(args.length == 0 || args[0].equalsIgnoreCase("help")){
                send(commandSource, "OneVersionRemake v%s");
                send(commandSource, "");
                send(commandSource, TextColor.AQUA, "ovr help");
                send(commandSource, TextColor.GRAY, "Shows this help.");
                send(commandSource, TextColor.AQUA, "ovr reload");
                send(commandSource, TextColor.GRAY, "Reloads the configuration file");
            }else
            if(args[0].equalsIgnoreCase("reload")){
                if(core.reloadConfig())
                    send(commandSource, TextColor.GREEN, "Successfully reloaded config.yml!");
                else
                    send(commandSource, TextColor.RED, "Error while reloading config.");
            }
        }
    }
    
    private void send(CommandSource commandSource, String msg, Object... args){
        send(commandSource, TextColor.WHITE, msg, args);
    }
    
    private void send(CommandSource commandSource, TextColor color, String msg, Object... args){
        commandSource.sendMessage(TextComponent.of(String.format(msg, args)).color(color));
    }
}
