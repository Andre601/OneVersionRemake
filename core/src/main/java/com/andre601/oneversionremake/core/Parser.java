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

package com.andre601.oneversionremake.core;

import com.andre601.oneversionremake.core.enums.ProtocolVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class Parser{
    
    public static Component toTextComponent(List<String> lines, List<Integer> serverProtocols, int userProtocol, boolean majorOnly){
        return MiniMessage.get().parse(parse(String.join("\n", lines), serverProtocols, userProtocol, majorOnly));
    }
    
    public static String toString(String text, List<Integer> serverProtocols, int userProtocol, boolean majorOnly){
        Component component = MiniMessage.get().parse(parse(text, serverProtocols, userProtocol, majorOnly));
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
    
    private static String parse(String text, List<Integer> serverProtocols, int userProtocol, boolean majorOnly){
        return text.replace("{version}", ProtocolVersion.getFriendlyNames(serverProtocols, majorOnly))
                .replace("{userVersion}", ProtocolVersion.getFriendlyName(userProtocol));
    }
    
}
