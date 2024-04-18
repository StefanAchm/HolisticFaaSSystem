package com.asi.hms.model.yaml;

import com.asi.hms.model.api.APIFunctionDeployment;

import java.util.HashMap;
import java.util.Map;

public class YamlFunctionDeployment {

    private String provider;
    private Integer memory;
    private Integer timeoutSecs;
    private String handler;
    private String region;
    private String runtime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("provider", provider);
        map.put("memory", memory);
        map.put("timeoutSecs", timeoutSecs);
        map.put("handler", handler);
        map.put("region", region);
        map.put("runtime", runtime);
        return map;
    }

    public YamlFunctionDeployment() {
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getTimeoutSecs() {
        return timeoutSecs;
    }

    public void setTimeoutSecs(Integer timeoutSecs) {
        this.timeoutSecs = timeoutSecs;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public static YamlFunctionDeployment fromApiFunctionDeployment(APIFunctionDeployment functionDeployment) {

        YamlFunctionDeployment yamlFunctionDeployment = new YamlFunctionDeployment();

        yamlFunctionDeployment.provider = functionDeployment.getProvider().name();
        yamlFunctionDeployment.memory = functionDeployment.getMemory();
        yamlFunctionDeployment.timeoutSecs = functionDeployment.getTimeoutSecs();
        yamlFunctionDeployment.handler = functionDeployment.getHandler();
        yamlFunctionDeployment.region = functionDeployment.getRegion();
        yamlFunctionDeployment.runtime = functionDeployment.getRuntime();

        return yamlFunctionDeployment;

    }

}
