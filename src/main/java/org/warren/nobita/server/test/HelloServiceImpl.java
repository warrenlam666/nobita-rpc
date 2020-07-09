package org.warren.nobita.server.test;

public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello() {
        return "hello! everyone";
    }

    @Override
    public String sayHi(String name) {
        try {
            return "hello! my dear "+name;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}