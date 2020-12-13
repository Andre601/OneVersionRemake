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

package com.andre601.oneversionremake.bungeecord.commands;

import com.andre601.oneversionremake.bungeecord.BungeeCore;
import com.andre601.oneversionremake.core.CommandPermissions;
import com.andre601.oneversionremake.core.enums.ProtocolVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class CmdOneVersionRemake extends Command{
    
    private final BungeeCore plugin;
    
    public CmdOneVersionRemake(BungeeCore plugin){
        super("oneversionremake", CommandPermissions.ADMIN, "ovr");
        this.plugin = plugin;
    }
    
    @Override
    public void execute(CommandSender commandSender, String[] args){
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            if(!commandSender.hasPermission(CommandPermissions.COMMAND_HELP)){
                sendMsg(commandSender, NamedTextColor.RED, "Insufficient permissions!");
                return;
            }
            
            sendMsg(commandSender, "OneVersionRemake v%s", plugin.getVersion());
            sendMsg(commandSender);
            sendMsg(commandSender, NamedTextColor.AQUA, "/ovr help");
            sendMsg(commandSender, NamedTextColor.GRAY, "Shows this command list.");
            sendMsg(commandSender);
            sendMsg(commandSender, NamedTextColor.AQUA, "/ovr reload");
            sendMsg(commandSender, NamedTextColor.GRAY, "Reloads the configuration.");
        }else
        if(args[0].equalsIgnoreCase("reload")){
            if(!commandSender.hasPermission(CommandPermissions.COMMAND_RELOAD)){
                sendMsg(commandSender, NamedTextColor.RED, "Insufficient permissions!");
                return;
            }
            
            if(plugin.reloadConfig()){
                List<Integer> serverProtocols = plugin.getConfigHandler().getIntList("Protocol", "Versions");
                
                sendMsg(commandSender, NamedTextColor.AQUA, "Loaded Minecraft Version(s):");
                if(serverProtocols.isEmpty()){
                    sendMsg(commandSender, NamedTextColor.RED, "None");
                }else{
                    sendMsg(commandSender, NamedTextColor.GRAY, ProtocolVersion.getFriendlyNames(serverProtocols, false));
                }
                
                sendMsg(commandSender);
                sendMsg(commandSender, NamedTextColor.GREEN, "Reload successful!");
            }else{
                sendMsg(commandSender, NamedTextColor.RED, "There was an error while reloading the config.yml");
                sendMsg(commandSender, NamedTextColor.RED, "Please check the console for any errors and warnings.");
            }
        }else{
            sendMsg(commandSender, NamedTextColor.RED, "Unknown argument \"%s\".", args[0]);
            sendMsg(commandSender, NamedTextColor.RED, "Run \"/ovr help\" for help");
        }
    }
    
    private void sendMsg(CommandSender sender){
        sendMsg(sender, "");
    }
    
    private void sendMsg(CommandSender sender, String msg, Object... args){
        sendMsg(sender, NamedTextColor.WHITE, msg, args);
    }
    
    private void sendMsg(CommandSender sender, NamedTextColor color, String msg, Object... args){
        sender.sendMessage(BungeeComponentSerializer.get()
                .serialize(Component.text(String.format(msg, args)).color(color))
        );
    }
}
