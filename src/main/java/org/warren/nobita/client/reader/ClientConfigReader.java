package org.warren.nobita.client.reader;

import org.warren.nobita.client.config.NobitaClientConfig;

import java.io.FileInputStream;
import java.io.InputStream;

public abstract class ClientConfigReader {

    String path;

    public ClientConfigReader(String path) {
        this.path = path;
    }

    public abstract NobitaClientConfig load(InputStream stream);

    public NobitaClientConfig load(){
        try {
            return load(new FileInputStream(path));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
