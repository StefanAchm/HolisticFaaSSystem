package com.asi.hms.model.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlFunctionType {

    private String name;
    private List<YamlFunctionImplementation> implementations;

    public YamlFunctionType() {
        this.implementations = new ArrayList<>();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("implementations", implementations);
        return map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<YamlFunctionImplementation> getImplementations() {
        return implementations;
    }

    public void setImplementations(List<YamlFunctionImplementation> implementations) {
        this.implementations = implementations;
    }

}
