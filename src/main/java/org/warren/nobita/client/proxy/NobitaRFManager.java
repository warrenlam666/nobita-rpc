package org.warren.nobita.client.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NobitaRFManager {

    public static Map<String, NobitaResponseFuture>  map = new ConcurrentHashMap<>();

    public static void put(String id, NobitaResponseFuture future){
        map.put(id, future);
    }

    public static NobitaResponseFuture get(String id){
        return map.get(id);
    }

    public static NobitaResponseFuture remove(String id){
        return map.remove(id);
    }

}
