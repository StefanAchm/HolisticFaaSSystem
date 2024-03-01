package com.asi.hms.model;


import com.asi.hms.enums.Provider;

public class DeployFunction {

    private Provider provider;

    private String filePath;
    private String name;
    private Integer memory;
    private Integer timeoutSecs;
    private String handler;
    private String region;
    private String runtime;

    public DeployFunction() {
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
