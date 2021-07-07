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

package com.andre601.oneversionremake.core.proxy;

import com.andre601.oneversionremake.core.OneVersionRemake;
import com.andre601.oneversionremake.core.interfaces.ProxyLogger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProtocolVersionResolver{
    private final OneVersionRemake core;
    private final HttpClient client = HttpClient.newHttpClient();
    
    private final ProxyLogger logger;
    
    private final File file;
    private final File path;
    
    private ConfigurationNode node = null;
    
    public ProtocolVersionResolver(OneVersionRemake core, Path path){
        this.core = core;
        
        this.logger = core.getProxyLogger();
        
        this.file = new File(path.toFile(), "versions.json");
        this.path = path.toFile();
    }
    
    public boolean loadFile(){
        if(!path.isDirectory() && !path.mkdirs()){
            logger.warn("Could not create folder for plugin!");
            return false;
        }
        
        try(InputStream is = updateCache()){
            if(is == null){
                logger.warn("Unable to create versions.json! InputStream was null.");
                return false;
            }
            
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException ex){
            logger.warn("Unable to create versions.json! Encountered IOException.", ex);
            return false;
        }
    
        GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .file(file)
                .build();
        
        try{
            node = loader.load();
        }catch(IOException ex){
            logger.warn("Could not load versions.json! Encountered IOException.", ex);
            return false;
        }
        
        return true;
    }
    
    public InputStream updateCache(){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("User-Agent", "OneVersionRemake")
                    .uri(new URI("https://raw.githubusercontent.com/Andre601/OneVersionRemake/master/versions.json"))
                    .build();
            
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            
            if(response.statusCode() != 200){
                logger.warn("Unable to establish connection! Site responded with non-successfull status-code " + response.statusCode() + "!");
                return null;
            }
            
            return response.body();
        }catch(URISyntaxException ex){
            core.getProxyLogger().warn("Unable to establish connection to retrieve Protocol Versions! URI was invalid!");
            return null;
        }catch(IOException ex){
            core.getProxyLogger().warn("Unable to establish connection to retrieve Protocol Versions! Request was non-successful!");
            return null;
        }catch(InterruptedException ex){
            core.getProxyLogger().warn("Unable to establish connection to retrieve Protocol Versions! Request was interrupted!");
            return null;
        }
    }
    
    public String getFriendlyNames(List<Integer> protocols, boolean majorOnly){
        Stream<Integer> stream = protocols.stream()
                .sorted(Comparator.reverseOrder());
        if(majorOnly){
            return stream.map(this::getMajor)
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        
        return stream.map(this::getFriendlyName)
                .collect(Collectors.joining(", "));
    }
    
    public String getFriendlyName(int protocolId){
        return fromPath(String.valueOf(protocolId), "name").getString("?");
    }
    
    public String getMajor(int protocolId){
        return fromPath(String.valueOf(protocolId), "major").getString("?");
    }
    
    private ConfigurationNode fromPath(Object... path){
        return node.node(path);
    }
}
