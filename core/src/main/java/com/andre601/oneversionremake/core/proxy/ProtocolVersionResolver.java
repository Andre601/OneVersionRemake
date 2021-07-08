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
    
    private final Path file;
    private final File path;
    
    private ConfigurationNode node = null;
    
    public ProtocolVersionResolver(OneVersionRemake core, Path path){
        this.core = core;
        
        this.logger = core.getProxyLogger();
        
        this.file = path.resolve("versions.json");
        this.path = path.toFile();
    }
    
    public boolean hasFile(){
        return file.toFile().exists();
    }
    
    public boolean loadFile(){
        if(!path.isDirectory() && !path.mkdirs()){
            logger.warn("Could not create folder for plugin!");
            return false;
        }
        
        try(InputStream is = updateCache()){
            if(is == null){
                return false;
            }
            
            Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException ex){
            logger.warn("Unable to create versions.json! Encountered IOException.", ex);
            return false;
        }
        
        return setupConfigurate();
    }
    
    public boolean setupConfigurate(){
        GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .file(file.toFile())
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
                String CONNECTION_ERR = "Unable to establish connection! Status-Code %d (%s)";
                
                switch(response.statusCode()){
                    case 404:
                        logger.warn(String.format(CONNECTION_ERR, response.statusCode(), "Site not available"));
                        logger.warn("Please report this to the Developer!");
                        return null;
                    
                    case 429:
                        logger.warn(String.format(CONNECTION_ERR, response.statusCode(), "Rate Limited"));
                        logger.warn("You connect too many times in a short period. Please delay any further restarts.");
                        return null;
                    
                    case 500:
                        logger.warn(String.format(CONNECTION_ERR, response.statusCode(), "Internal Server Error"));
                        logger.warn("The Server (GitHub) had an unexpected error when handling the request. Try again later.");
                        return null;
                    
                    default:
                        logger.warn("Encountered unknown Response code " + response.statusCode());
                        logger.warn("Inform the developer about this on their Discord. This is NOT a bug however!");
                        return null;
                }
            }
            
            return response.body();
        }catch(Exception ex){
            if(ex instanceof URISyntaxException){
                core.getProxyLogger().warn("Unable to establish connection to retrieve Protocol Versions! URI was invalid!");
            }else
            if(ex instanceof IOException){
                core.getProxyLogger().warn("Unable to establish connection to retrieve Protocol Versions! Request was non-successful!");
            }else
            if(ex instanceof InterruptedException){
                core.getProxyLogger().warn("Unable to establish connection to retrieve Protocol Versions! Request was interrupted!");
            }else{
                core.getProxyLogger().warn("Unable to establish connection to retrieve Protocol Versions! Received unknown Exception!", ex);
            }
            
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
