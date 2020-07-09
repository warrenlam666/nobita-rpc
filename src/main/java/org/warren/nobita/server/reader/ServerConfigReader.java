package org.warren.nobita.server.reader;

import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.server.config.ServerConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public abstract class ServerConfigReader {

    private String path;

    public ServerConfigReader(String path){
        this.path = path;
    }

    public ServerConfig load(){
        try {
            log.info("loading the server config from {}", path);
            return load(new FileInputStream(path));
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /*
    由子类实现具体加载逻辑
    * */
   public abstract ServerConfig load(InputStream inputStream);

}
