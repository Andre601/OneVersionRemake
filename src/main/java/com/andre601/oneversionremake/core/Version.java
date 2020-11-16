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

import java.util.List;
import java.util.stream.Collectors;

public enum Version{
    MC_1_16_4(0, ""),
    MC_1_16_3(0, ""),
    MC_1_16_2(0, ""),
    MC_1_16_1(0, ""),
    MC_1_16(0, ""),
    MC_1_15_2(0, ""),
    MC_1_15_1(0, ""),
    MC_1_15(0, ""),
    MC_1_14_4(0, ""),
    MC_1_14_3(0, ""),
    MC_1_14_2(0, ""),
    MC_1_14_1(0, ""),
    MC_1_14(0, ""),
    MC_1_13_2(0, ""),
    MC_1_13_1(0, ""),
    MC_1_13(0, ""),
    MC_1_12_2(0, ""),
    MC_1_12_1(0, ""),
    MC_1_12(0, ""),
    MC_1_11_2(0, ""),
    MC_1_11(0, ""),
    MC_1_10_2(0, ""),
    MC_1_9_4(0, ""),
    MC_1_9_2(0, ""),
    MC_1_9_1(0, ""),
    MC_1_9(0, ""),
    MC_1_8_9(0, ""),
    UNKNOWN(0, "");
    
    private final int protocol;
    private final String friendlyName;
    
    public Version(int protocol, String friendlyName){
        this.protocol = protocol;
        this.friendlyName = friendlyName;
    }
    
    public static String getFriendlyName(int protocol){
        for(Version version : values()){
            if(version.getProtocol() == protocol)
                return version.getFriendlyName();
        }
        
        return UNKNOWN.getFriendlyName();
    }
    
    public static String getFriendlyNames(List<Integer> protocols){
        return protocols.stream()
                .sorted()
                .map(Version::getFriendlyName)
                .collect(Collectors.joining(", "));
    }
    
    private int getProtocol(){
        return protocol;
    }
    
    private String getFriendlyName(){
        return friendlyName;
    }
}
