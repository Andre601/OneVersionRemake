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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProtocolVersionResolver{
    private final OkHttpClient CLIENT = new OkHttpClient();
    private final Gson gson = new Gson();
    
    private final ProxyLogger logger;
    
    private final Path file;
    
    private VersionsFile versions = null;
    
    public ProtocolVersionResolver(OneVersionRemake core, Path path){
        this.logger = core.getProxyLogger();
        
        this.file = path.resolve("versions.json");
    }
    
    public VersionsFile getVersions(){
        return versions;
    }
    
    public boolean isFileMissing(){
        return !file.toFile().exists();
    }
    
    public CompletableFuture<VersionsFile> createFile(String url){
        return CompletableFuture.supplyAsync(() -> (copyAndUpdate(getSiteJson(url))));
    }
    
    public CompletableFuture<VersionsFile> updateFile(String url){
        return CompletableFuture.supplyAsync(() -> {
            String json = getSiteJson(url);
            if(json == null)
                return null;
            
            try{
                VersionsFile currentVersions = getVersionsFile(Files.readAllLines(file));
                VersionsFile newVersions = getVersionsFile(json);
                
                if(currentVersions == null || newVersions == null){
                    logger.warn("Error while getting current and new versions info.");
                    logger.warnFormat("Current null? %b; New null? %b", currentVersions == null, newVersions == null);
                    return null;
                }
                
                // User is using old versions.json URL
                if(newVersions.getFileVersion() == -1){
                    logger.warn("Remote JSON file does not have a 'file_version' property set!");
                    logger.warn("Make sure the URL points to an updated version.");
                    logger.warnFormat("New URL: %s", OneVersionRemake.DEF_VERSIONS_URL);
                    
                    return null;
                }
                
                if(currentVersions.getFileVersion() < newVersions.getFileVersion()){
                    logger.info("Current versions.json is outdated. Updating...");
                    return copyAndUpdate(json);
                }else{
                    logger.info("Current versions.json is up-to-date!");
                    return (versions = currentVersions);
                }
            }catch(IOException ex){
                logger.warn("Encountered IOException while reading versions.json file.", ex);
                return null;
            }
        });
    }
    
    public CompletableFuture<VersionsFile> loadFile(){
        return CompletableFuture.supplyAsync(() -> {
            try{
                return (versions = getVersionsFile(Files.readAllLines(file)));
            }catch(IOException ex){
                logger.warn("Encountered IOException while trying to load versions.json");
                return null;
            }
        });
    }
    
    private VersionsFile copyAndUpdate(String json){
        if(json == null)
            return null;
        
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        
        try{
            Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
            return (versions = getVersionsFile(json));
        }catch(IOException ex){
            logger.warn("Encountered IOException while saving the versions.json file.", ex);
            return null;
        }
    }
    
    private VersionsFile getVersionsFile(List<String> lines){
        return getVersionsFile(String.join("", lines));
    }
    
    private VersionsFile getVersionsFile(String json){
        try{
            return gson.fromJson(json, VersionsFile.class);
        }catch(JsonSyntaxException ex){
            logger.warn("Encountered JsonSyntaxException while parsing JSON.", ex);
            return null;
        }
    }
    
    private String getSiteJson(String url){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "OneVersionRemake")
                .build();
        
        try(Response response = CLIENT.newCall(request).execute()){
            if(!response.isSuccessful()){
                logger.warnFormat("Received non-successful response code from %s. Further details below.", url);
                
                switch(response.code()){
                    case 404:
                        logger.warn("404: Unknown Site. Make sure the URL is valid.");
                        break;
                    
                    case 429:
                        logger.warn("429: Encountered rate limit. Please delay any future requests.");
                        break;
                    
                    case 500:
                        logger.warn("500: Site couldn't process request and encountered an 'Internal Server Error'. Try again later?");
                        break;
                    
                    default:
                        logger.warnFormat("%d: %s", response.code(), response.message());
                        break;
                }
                
                return null;
            }
            
            ResponseBody body = response.body();
            if(body == null){
                logger.warn("Received null response body.");
                return null;
            }
            
            String json = body.string();
            if(json.isEmpty()){
                logger.warn("Received empty response body.");
                return null;
            }
            
            return json;
        }catch(IOException ex){
            logger.warn("Encountered IOException!", ex);
            return null;
        }
    }
}
