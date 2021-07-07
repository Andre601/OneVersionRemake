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

package com.andre601.oneversionremake.core.enums;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum ProtocolVersion{
    MC_1_17_1(756, "1.17.1", "1.17.x"),
    MC_1_17  (755, "1.17",   "1.17.x"),
    
    MC_1_16_5(754, "1.16.5", "1.16.x"),
    MC_1_16_3(753, "1.16.3", "1.16.x"),
    MC_1_16_2(751, "1.16.2", "1.16.x"),
    MC_1_16_1(736, "1.16.1", "1.16.x"),
    MC_1_16  (735, "1.16",   "1.16.x"),
    
    MC_1_15_2(578, "1.15.2", "1.15.x"),
    MC_1_15_1(575, "1.15.1", "1.15.x"),
    MC_1_15  (573, "1.15",   "1.15.x"),
    
    MC_1_14_4(498, "1.14.4", "1.14.x"),
    MC_1_14_3(490, "1.14.3", "1.14.x"),
    MC_1_14_2(485, "1.14.2", "1.14.x"),
    MC_1_14_1(480, "1.14.1", "1.14.x"),
    MC_1_14  (477, "1.14",   "1.14.x"),
    
    MC_1_13_2(404, "1.13.2", "1.13.x"),
    MC_1_13_1(401, "1.13.1", "1.13.x"),
    MC_1_13  (393, "1.13",   "1.13.x"),
    
    MC_1_12_2(340, "1.12.2", "1.12.x"),
    MC_1_12_1(338, "1.12.1", "1.12.x"),
    MC_1_12  (335, "1.12",   "1.12.x"),
    
    MC_1_11_2(316, "1.11.2", "1.11.x"),
    MC_1_11  (315, "1.11",   "1.11.x"),
    
    MC_1_10_2(210, "1.10.2", "1.10.2"),
    
    MC_1_9_4 (110, "1.9.4",  "1.9.x"),
    MC_1_9_2 (109, "1.9.2",  "1.9.x"),
    MC_1_9_1 (108, "1.9.1",  "1.9.x"),
    MC_1_9   (107, "1.9",    "1.9.x"),
    
    MC_1_8_9 (47,  "1.8.9",  "1.8.9"),
    
    // In case none of the above versions match
    UNKNOWN  (0,   "?", "?");
    
    private final int protocol;
    private final String friendlyName;
    private final String major;
    
    ProtocolVersion(int protocol, String friendlyName, String major){
        this.protocol = protocol;
        this.friendlyName = friendlyName;
        this.major = major;
    }
    
    public static String getFriendlyName(int protocol){
        for(ProtocolVersion version : values()){
            if(version.getProtocol() == protocol)
                return version.getFriendlyName();
        }
        
        return UNKNOWN.getFriendlyName();
    }
    
    public static String getFriendlyNames(List<Integer> protocols, boolean majorOnly){
        if(majorOnly){
            return protocols.stream()
                    .sorted(Comparator.reverseOrder())
                    .map(ProtocolVersion::getMajor)
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        
        return protocols.stream()
                .sorted(Comparator.reverseOrder())
                .map(ProtocolVersion::getFriendlyName)
                .collect(Collectors.joining(", "));
    }
    
    public static String getMajor(int protocol){
        for(ProtocolVersion version : values()){
            if(version.getProtocol() == protocol)
                return version.getMajor();
        }
    
        return UNKNOWN.getMajor();
    }
    
    private int getProtocol(){
        return protocol;
    }
    
    private String getFriendlyName(){
        return friendlyName;
    }
    
    private String getMajor(){
        return major;
    }
}
