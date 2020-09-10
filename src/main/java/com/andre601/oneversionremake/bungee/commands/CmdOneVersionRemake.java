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

package com.andre601.oneversionremake.bungee.commands;

import com.andre601.oneversionremake.bungee.BungeeCore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdOneVersionRemake extends Command{
    
    private final BungeeCore core;
    
    public CmdOneVersionRemake(BungeeCore core){
        super("oneversionremake", "oneversionremake.admin", "ovr");
        this.core = core;
        core.getProxy().getPluginManager().registerCommand(core, this);
    }
    
    @Override
    public void execute(CommandSender commandSender, String[] args){
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer)commandSender;
            
            if(args.length == 0 || args[0].equalsIgnoreCase("help")){
                if(!player.hasPermission("oneversionremake.command.help")){
                    send(player, "&cInsufficient permissions!");
                    return;
                }
                
                send(player, "OneVersionRemake v%s", core.getVersion());
                send(player, "");
                send(player, "&b/ovr help");
                send(player, "&7Shows this help.");
                send(player, "&b/ovr reload");
                send(player, "&7Reloads the configuration file.");
            }else
            if(args[0].equalsIgnoreCase("reload")){
                if(!player.hasPermission("oneversionremake.command.reload")){
                    send(player, "&cInsufficient permissions!");
                    return;
                }
                
                if(core.reloadConfig())
                    send(player, "&aSuccessfully reloaded config.yml!");
                else
                    send(player, "&cError while reloading the config. Please check the console!");
            }
        }else{
            if(args.length == 0 || args[0].equalsIgnoreCase("help")){
                send(commandSender, "OneVersionRemake v%s", core.getVersion());
                send(commandSender, "");
                send(commandSender, "&bovr help");
                send(commandSender, "&7Shows this help.");
                send(commandSender, "&bovr reload");
                send(commandSender, "&7Reloads the configuration file.");
            }else
            if(args[0].equalsIgnoreCase("reload")){
                if(core.reloadConfig())
                    send(commandSender, "&aSuccessfully reloaded config.yml");
                else
                    send(commandSender, "&cError while reloading the config.");
            }
        }
    }
    
    private void send(CommandSender sender, String msg, Object... args){
        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', String.format(
                msg,
                args
        ))));
    }
}
