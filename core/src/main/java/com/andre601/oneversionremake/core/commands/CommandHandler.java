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

package com.andre601.oneversionremake.core.commands;

import com.andre601.oneversionremake.core.CommandPermissions;
import com.andre601.oneversionremake.core.OneVersionRemake;
import com.andre601.oneversionremake.core.interfaces.CmdSender;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class CommandHandler{
    
    public final OneVersionRemake core;
    
    public CommandHandler(OneVersionRemake core){
        this.core = core;
    }
    
    public void handle(CmdSender sender, String[] args){
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            if(!sender.hasPermission(CommandPermissions.COMMAND_HELP)){
                sender.sendMsg(NamedTextColor.RED, "Insufficient permissions!");
                return;
            }
            
            sender.sendMsg("OneVersionRemake v%s", core.getVersion());
            sender.sendMsg();
            sender.sendMsg(NamedTextColor.AQUA, "/ovr help");
            sender.sendMsg(NamedTextColor.GRAY, "Shows this Command list.");
            sender.sendMsg();
            sender.sendMsg(NamedTextColor.AQUA, "/ovr reload");
            sender.sendMsg(NamedTextColor.GRAY, "Reloads the configuration file.");
            sender.sendMsg();
            sender.sendMsg(NamedTextColor.AQUA, "/ovr refresh");
            sender.sendMsg(NamedTextColor.GRAY, "Updates the versions.json file.");
        }else
        if(args[0].equalsIgnoreCase("reload")){
            if(!sender.hasPermission(CommandPermissions.COMMAND_RELOAD)){
                sender.sendMsg(NamedTextColor.RED, "Insufficient permissions!");
                return;
            }
            
            sender.sendMsg(NamedTextColor.GRAY, "Reloading config.yml...");
            
            if(core.reloadConfig()){
                List<Integer> protocols = core.getConfigHandler().getIntList("Protocol", "Versions");
                
                sender.sendMsg(NamedTextColor.AQUA, "Loaded following Minecraft version(s):");
                if(protocols.isEmpty()){
                    sender.sendMsg(NamedTextColor.RED, "None");
                }else{
                    sender.sendMsg(NamedTextColor.GRAY, core.getProtocolVersionResolver().getVersions().getFriendlyNames(protocols, false, false));
                }
                
                sender.sendMsg();
                sender.sendMsg(NamedTextColor.GREEN, "Reload successful!");
            }else{
                sender.sendMsg(NamedTextColor.RED, "There was an issue while reloading the configuration file!");
                sender.sendMsg(NamedTextColor.RED, "Please check the Console of the Proxy for any errors and warnings.");
            }
        }else
        if(args[0].equalsIgnoreCase("refresh")){
            if(!sender.hasPermission(CommandPermissions.COMMAND_REFRESH)){
                sender.sendMsg(NamedTextColor.RED, "Insufficient permissions!");
                return;
            }
            
            sender.sendMsg(NamedTextColor.GRAY, "Updating versions.json...");
            
            core.getProtocolVersionResolver()
                    .updateFile(core.getConfigHandler().getString(OneVersionRemake.DEF_VERSIONS_URL, "Settings", "VersionsUrl"))
                    .whenComplete((versions, throwable) -> {
                        if(versions == null || throwable != null){
                            if(throwable != null){
                                sender.sendMsg(NamedTextColor.RED, "Encountered an Exception while performing the update.");
                            }else{
                                sender.sendMsg(NamedTextColor.RED, "Update was not successful! Check console for any errors!");
                            }
                            
                            return;
                        }
                        
                        sender.sendMsg(NamedTextColor.GREEN, "Successfully updated versions.json!");
                    });
        }else{
            sender.sendMsg(NamedTextColor.RED, "Unknown argument \"%s\".", args[0]);
            sender.sendMsg(NamedTextColor.RED, "Run \"/ovr help\" for all commands.");
        }
    }
}
