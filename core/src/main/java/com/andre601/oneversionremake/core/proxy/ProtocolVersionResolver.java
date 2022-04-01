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
import org.json.JSONException;
import org.json.JSONObject;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProtocolVersionResolver{
    private final OkHttpClient CLIENT = new OkHttpClient();
    
    private final ProxyLogger logger;
    
    private final Path file;
    
    private ConfigurationNode node = null;
    
    public ProtocolVersionResolver(OneVersionRemake core, Path path){
        this.logger = core.getProxyLogger();
        
        this.file = path.resolve("versions.json");
    }
    
    public boolean isFileMissing(){
        return !file.toFile().exists();
    }
    
    public CompletableFuture<Boolean> createFile(String url){
        return CompletableFuture.supplyAsync(() -> {
            JSONObject json = getSiteJSON(url);
            if(json == null)
                return false;
            
            return copyAndLoad(json);
        });
    }
    
    public CompletableFuture<Boolean> updateFile(String url){
        return CompletableFuture.supplyAsync(() -> {
            JSONObject json = getSiteJSON(url);
            if(json == null)
                return false;
            
            ConfigurationNode temp = getNodeInstance();
            if(temp == null)
                return false;
            
            int currentVer = temp.node("file_version").getInt(-1);
            int newVer = json.optInt("file_version", -1);
            
            if(newVer > currentVer){
                logger.info("Current versions.json is outdated! Updating...");
                
                return copyAndLoad(json);
            }else{
                logger.info("Current versions.json is up-to-date!");
                return loadConfigurate();
            }
        });
    }
    
    public boolean loadConfigurate(){
        return (node = getNodeInstance()) != null;
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
    
    private boolean copyAndLoad(JSONObject json){
        InputStream is = new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
        
        try{
            Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
            return loadConfigurate();
        }catch(IOException ex){
            logger.warn("Encountered IOException while copying versions.json file.");
            return false;
        }
    }
    
    private ConfigurationNode getNodeInstance(){
        GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .file(file.toFile())
                .build();
        
        try{
            return loader.load();
        }catch(IOException ex){
            logger.warn("Encountered IOException while loading the ConfigurationNode.");
            return null;
        }
    }
    
    private JSONObject getSiteJSON(String url){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "OneVersionRemake")
                .build();
        
        try(Response response = CLIENT.newCall(request).execute()){
            if(!response.isSuccessful()){
                logger.warn(String.format(
                        "Received non-successfull response code from %s. Further details below.",
                        url
                ));
                
                switch(response.code()){
                    case 404:
                        logger.warn("404: Siteis not available. Perhaps a malformed URL?");
                        break;
                    
                    case 429:
                        logger.warn("429: Too many requests. Please delay any further requests.");
                        break;
                    
                    case 500:
                        logger.warn("500: Internal Server Error. Try again later.");
                        break;
                    
                    default:
                        logger.warnFormat("%d: Unknown Status code. Please report this to the developer of the plugin!", response.code());
                        break;
                }
                return null;
            }
            
            ResponseBody body = response.body();
            if(body == null){
                logger.warn("Received empty response Body.");
                return null;
            }
            
            String json = body.string();
            if(json.isEmpty()){
                logger.warn("Received empty response Body.");
                return null;
            }
            
            return new JSONObject(json);
        }catch(IOException ex){
            logger.warn(String.format(
                    "Encountered IOException: %s",
                    ex.getMessage()
            ));
            return null;
        }catch(JSONException ex){
            logger.warn("Received malformed JSON response body!");
            return null;
        }
    }
    
    private ConfigurationNode fromPath(Object... path){
        return node.node(path);
    }
}
