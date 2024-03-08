/*
 * Copyright 2020 - 2022 Andre601
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

package com.andre601.oneversionremake.core.proxy;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VersionsFile{
    
    @SerializedName("file_version")
    private int fileVersion = -1;
    @SerializedName("protocols")
    private ProtocolInfo[] protocolInfos = new ProtocolInfo[0];
    
    public int getFileVersion(){
        return fileVersion;
    }
    
    public String getFriendlyNames(List<Integer> protocols, boolean majorOnly, boolean blacklist){
        Stream<Integer> stream;
        
        if(blacklist){
            stream = Arrays.stream(protocolInfos)
                    .map(ProtocolInfo::getProtocol)
                    .filter(protocol -> !protocols.contains(protocol))
                    .sorted(Comparator.reverseOrder());
        }else{
            stream = protocols.stream()
                    .sorted(Comparator.reverseOrder());
        }
        
        if(majorOnly){
            return stream.map(this::getMajor)
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        
        return stream.map(this::getFriendlyName)
                .collect(Collectors.joining(", "));
    }
    
    public String getFriendlyName(int protocol){
        ProtocolInfo protocolInfo = getProtocolInfo(protocol);
        if(protocolInfo == null)
            return "?";
        
        return protocolInfo.getName();
    }
    
    public String getMajor(int protocol){
        ProtocolInfo protocolInfo = getProtocolInfo(protocol);
        if(protocolInfo == null)
            return "?";
        
        return protocolInfo.getMajor();
    }
    
    private ProtocolInfo getProtocolInfo(int protocol){
        for(ProtocolInfo protocolInfo : protocolInfos){
            if(protocolInfo.getProtocol() == protocol)
                return protocolInfo;
        }
        
        return null;
    }
    
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private static class ProtocolInfo{
        private int protocol = 0;
        private String major = "UNKNOWN";
        private String name = "UNKNOWN";
    
        public int getProtocol(){
            return protocol;
        }
    
        public String getMajor(){
            return major;
        }
    
        public String getName(){
            return name;
        }
    }
}
