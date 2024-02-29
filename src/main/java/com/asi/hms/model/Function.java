package com.asi.hms.model;

import com.asi.hms.enums.Region;

import java.net.URI;
import java.nio.file.Path;

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


// https://docs.aws.amazon.com/lambda/latest/dg/configuration-function-common.html
public class Function {

    /**
     * The source of the function. <br>
     * It can be an archive, a URL, or a path.
     */
    private Path filePath;

    /**
     * The name of the function.
     */
    private String name;

    /**
     * The maximum amount of memory that the function can use at runtime.
     * TODO: check which unit (and possible values) is used for memory for all providers
     *
     * AWS: a value between 128 MB and 10,240 MB in 1-MB increments. At 1,769 MB, a function has the equivalent of one vCPU (one vCPU-second of credits per second).
     */
    private Integer memory;

    /**
     * The maximum execution time for the function (in seconds)
     * TODO: check which unit (and possible values) is used for timeout for all providers
     *
     * AWS: The default value for this setting is 3 seconds, but you can adjust this in increments of 1 second up to a maximum value of 15 minutes.
     * https://docs.aws.amazon.com/lambda/latest/api/API_CreateFunction.html#lambda-CreateFunction-request-Timeout
     */
    private Integer timeoutSecs;

    /**
     * The handler of the function.
     */
    private String handler;

    /**
     * The regions, where the function is deployed.
     * TODO: check which regions are available for all providers
     *
     *
     * GCP: https://cloud.google.com/compute/docs/regions-zones/viewing-regions-zones?hl=de#rest
     */
    private Region region;

    /**
     * The runtime of the function.
     * TODO: check which runtimes are available for all providers
     */
    private String runtime;

    /**
     * The name of the project, where the function is deployed. <br>
     * (Currently only used for GCP)
     */
    private String projectName;

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
