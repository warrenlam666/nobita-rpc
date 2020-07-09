package org.warren.nobita.client.config;

import lombok.Data;

@Data
public class GlobalInfo {

    private int timeout;

    private String[] defaultServerAddr;

    private Register register;


    @Data
    class Register{
        public String serverAddr;
        public String type;
    }
}
