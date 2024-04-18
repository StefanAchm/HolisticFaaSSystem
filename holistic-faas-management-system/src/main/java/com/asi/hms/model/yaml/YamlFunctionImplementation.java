package com.asi.hms.model.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlFunctionImplementation {

    private String path;
    private List<YamlFunctionDeployment> deployments;

    public YamlFunctionImplementation() {
        this.deployments = new ArrayList<>();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        map.put("deployments", deployments);
        return map;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<YamlFunctionDeployment> getDeployments() {
        return deployments;
    }

    public void setDeployments(List<YamlFunctionDeployment> deployments) {
        this.deployments = deployments;
    }
}
