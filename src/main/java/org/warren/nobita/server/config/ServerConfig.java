package org.warren.nobita.server.config;

import lombok.Data;

import java.util.List;

@Data
public class ServerConfig {

    private String serverHost;
    private int serverPort;

    private List<ServiceInfo> services;

    private Register register;


}
