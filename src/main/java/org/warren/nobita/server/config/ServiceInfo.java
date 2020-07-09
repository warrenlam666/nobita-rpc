package org.warren.nobita.server.config;

import lombok.Data;

@Data
public class ServiceInfo {

    private String interface_name ;    //interface为保留字

    private String implement_name;

    private boolean lazy;


}
