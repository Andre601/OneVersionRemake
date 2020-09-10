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

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/*
 * From ServerListPlus
 * https://github.com/Minecrell/ServerListPlus/blob/master/src/main/java/net/minecrell/serverlistplus/core/util/SnakeYAML.java
 */
public class SnakeYAML{
    
    private static final String MAVEN_CENTRAL = "https://repo.maven.apache.org/maven2/";
    
    private static final String YAML_VERSION = "1.19";
    private static final String SNAKE_YAML_JAR = "snakeyaml-" + YAML_VERSION + ".jar";
    private static final String SNAKE_YAML =
            MAVEN_CENTRAL + "org/yaml/snakeyaml/" + YAML_VERSION + '/' + SNAKE_YAML_JAR;
    
    private static final String EXPECTED_HASH = "2d998d3d674b172a588e54ab619854d073f555b5";
    
    public static Path load(Path pluginFolder) throws IOException{
        Path libFolder = pluginFolder.resolve("libraries");
        Path path = libFolder.resolve(SNAKE_YAML_JAR);
        
        if(Files.notExists(path)){
            Files.createDirectories(libFolder);
            
            URL url = new URL(SNAKE_YAML);
            
            String hash;
            
            try(HashingInputStream his = new HashingInputStream(Hashing.sha1(), url.openStream());
                ReadableByteChannel source = Channels.newChannel(his);
                FileChannel out = FileChannel.open(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)){
                
                out.transferFrom(source, 0, Long.MAX_VALUE);
                hash = his.hash().toString();
            }
            
            if(!hash.equals(EXPECTED_HASH)){
                Files.delete(path);
                throw new IOException("Hash mismatch in " + SNAKE_YAML_JAR + ": Expected " + EXPECTED_HASH);
            }
        }
        
        return path;
    }
}
