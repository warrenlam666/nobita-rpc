package org.warren.nobita.server.reader;

import com.google.gson.Gson;
import org.warren.nobita.server.config.ServerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonServerConfigReader extends ServerConfigReader {

    public ServerConfig load(InputStream inputStream) {
        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), ServerConfig.class);
    }

    public JsonServerConfigReader(String path) {
        super(path);
    }

    public static void main(String[] args) {
        ServerConfigReader reader = new JsonServerConfigReader(JsonServerConfigReader.class.getResource("/nobita-config.json").getPath());
        ServerConfig config = reader.load();
        System.out.println(config);
    }

}
