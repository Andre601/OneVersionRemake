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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProtocolVersionResolver{
    private final OkHttpClient CLIENT = new OkHttpClient();
    
    private final ProxyLogger logger;
    
    private final Path file;
    private final Path path;
    
    private ConfigurationNode node = null;
    
    public ProtocolVersionResolver(OneVersionRemake core, Path path){
        this.logger = core.getProxyLogger();
        
        this.file = path.resolve("versions.json");
        this.path = path;
    }
    
    public boolean hasFile(){
        return file.toFile().exists();
    }
    
    public boolean loadFile(){
        if(!path.toFile().isDirectory() && !path.toFile().mkdirs()){
            logger.warn("Could not create folder for plugin!");
            return false;
        }
        
        return updateCache();
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
    
    public boolean updateCache(){
        String url = "https://raw.githubusercontent.com/Andre601/OneVersionRemake/master/versions.json";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "OneVersionRemake")
                .build();
        
        try(Response response = CLIENT.newCall(request).execute()){
            if(!response.isSuccessful()){
                logger.warn(String.format(
                        "Unable to establish connection! Status-Code %d (%s) received!",
                        response.code(),
                        response.message()
                ));
                switch(response.code()){
                    case 404:
                        logger.warn(String.format(
                                "The requested site (%s) does not exist. Please report this to the developer on Discord!",
                                url
                        ));
                        break;
                        
                        case 429:
                            logger.warn("Encountered a Rate Limit. Please delay any future Proxy Restarts to avoid this.");
                            break;
                        
                        case 500:
                            logger.warn("The Site (GitHub.com) encountered an error when handling the request. Try again later...");
                            break;
                        
                        default:
                            logger.warn("This is an unknown error by the plugin! Please report this to the developer on Discord!");
                            break;
                }
                return false;
            }
            
            ResponseBody body = response.body();
            if(body == null){
                logger.warn("GitHub.com returned an invalid/empty body!");
                return false;
            }
            
            Files.copy(body.byteStream(), file, StandardCopyOption.REPLACE_EXISTING);
            
            return setupConfigurate();
        }catch(IOException ex){
            logger.warn("Encountered IOException while performing a request!", ex);
            return false;
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
