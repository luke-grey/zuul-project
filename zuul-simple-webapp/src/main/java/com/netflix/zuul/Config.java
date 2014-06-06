package com.netflix.zuul;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.core.io.ClassPathResource;

/**
 * Config.java
 *
 * @author Kevin Albert | productOps, Inc.
 * With an accessor method by Luke Grey
 */
public class Config {

    private Map<String, String> keyValueMap;
    public Config(String filename) {
        ObjectMapper om = new ObjectMapper();
        try {
            keyValueMap = om.readValue(new ClassPathResource(filename).getFile(),
                    new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            keyValueMap = new HashMap<String, String>();
        }
    }
    public Map<String,String> getMap(){
    	return keyValueMap;
    }

    public String get(String key) {
        return keyValueMap.get(key);
    }
}