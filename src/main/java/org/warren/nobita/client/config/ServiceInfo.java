package org.warren.nobita.client.config;

import lombok.Data;

@Data
public class ServiceInfo {

    private String interfaceName;

    private String[] serverAddr;

    private String timeout;

}
