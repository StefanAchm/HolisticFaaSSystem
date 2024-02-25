package com.asi.hms.model;

// functions:
//  - archive: "https://bucket.s3.region.amazonaws.com/../.."
//    name: "function1"
//    memory: 128
//    timeout: 60
//    providers:
//      - name: "AWS"
//        handler: "example.AWSHandler"
//        regions:
//          - "us-east-1"
//          - "us-west-2"
//        runtime: "go1.x"
//      - name: "Google"
//        handler: "example.GoogleHandler"
//        regions:
//          - "us-central1"
//        runtime: "go116"
//  - archive: "https://storage.cloud.google.com/bucket/../.."
//    name: "function2"

import com.asi.hms.enums.Region;

public class Function {

    /**
     * The source of the function. <br>
     * It can be an archive, a URL, or a path.
     */
    private String source;

    /**
     * The name of the function.
     */
    private String name;

    /**
     * The maximum amount of memory that the function can use at runtime.
     * TODO: check which unit (and possible values) is used for memory for all providers
     */
    private Integer memory;

    /**
     * The maximum execution time for the function.
     * TODO: check which unit (and possible values) is used for timeout for all providers
     */
    private Integer timeout;

    /**
     * The provider, where to deploy the function.
     * TODO: use enum or a class for providers
     */
    private String provider;

    /**
     * The handler of the function.
     */
    private String handler;

    /**
     * The regions, where the function is deployed.
     * TODO: check which regions are available for all providers
     */
    private Region region;

    /**
     * The runtime of the function.
     * TODO: check which runtimes are available for all providers
     */
    private String runtime;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}
