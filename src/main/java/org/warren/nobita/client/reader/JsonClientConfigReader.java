package org.warren.nobita.client.reader;

import com.google.gson.Gson;
import org.warren.nobita.client.config.NobitaClientConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonClientConfigReader extends ClientConfigReader {

    public JsonClientConfigReader(String path) {
        super(path);
    }

    @Override
    public NobitaClientConfig load(InputStream stream) {
        Gson gson = new Gson();
        NobitaClientConfig clientConfig = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),NobitaClientConfig.class);
        System.out.println(clientConfig);
        return clientConfig;
    }

}
