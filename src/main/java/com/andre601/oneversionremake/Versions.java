/*
 * Copyright 2019 Andre601
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

package com.andre601.oneversionremake;

public enum Versions{
    MC_1_14_4(498, "1.14.4"),
    MC_1_14_3(490, "1.14.3"),
    MC_1_14_2(485, "1.14.2"),
    MC_1_14_1(480, "1.14.1"),
    MC_1_14  (477, "1.14"),
    MC_1_13_2(404, "1.13.2"),
    MC_1_13_1(401, "1.13.1"),
    MC_1_13  (393, "1.13"),
    MC_1_12_2(340, "1.12.2"),
    MC_1_12_1(338, "1.12.1"),
    MC_1_12  (335, "1.12"),
    MC_1_11_2(316, "1.11.2"),
    MC_1_11  (315, "1.11"),
    MC_1_10_2(210, "1.10.2"),
    MC_1_9_4 (110, "1.9.4"),
    MC_1_9_2 (109, "1.9.2"),
    MC_1_9_1 (108, "1.9.1"),
    MC_1_9   (107, "1.9"),
    MC_1_8_9 (47,  "1.8"),
    UNKNOWN  (0,   "?");
    
    private int protocol;
    private String name;
    
    Versions(int protocol, String name){
        this.protocol = protocol;
        this.name = name;
    }
    
    private String getName(){
        return name;
    }
    
    private int getProtocol(){
        return protocol;
    }
    
    public static String getFriendlyName(int protocol){
        for(Versions versions : values()){
            if(versions.getProtocol() == protocol)
                return versions.getName();
        }
        return UNKNOWN.getName();
    }
}
