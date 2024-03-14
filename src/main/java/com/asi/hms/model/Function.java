package com.asi.hms.model;

import com.asi.hms.enums.*;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.utils.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.asi.hms.enums.Provider.AWS;
import static com.asi.hms.enums.Provider.GCP;

public class Function {

    private UUID id;

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
     * <p>
     * AWS: a value between 128 MB and 10,240 MB in 1-MB increments. At 1,769 MB, a function has the equivalent of one vCPU (one vCPU-second of credits per second).
     */
    private Integer memory;

    /**
     * The maximum execution time for the function (in seconds)
     * TODO: check which unit (and possible values) is used for timeout for all providers
     * <p>
     * AWS:
     *      The default value for this setting is 3 seconds, but you can adjust this in increments of 1 second up to a maximum value of 15 minutes.
     *      See <a href="https://docs.aws.amazon.com/lambda/latest/api/API_CreateFunction.html#lambda-CreateFunction-request-Time">this</a>
     */
    private Integer timeoutSecs;

    /**
     * The handler of the function.
     */
    private String handler;

    /**
     * The region, where the function will be deployed to.
     */
    private RegionInterface region;

    /**
     * The runtime of the function.
     */
    private RuntimeInterface runtime;

    public static Function fromDbFunction(DBFunctionDeployment dbFunctionDeployment) {

        DBFunction dbFunction = dbFunctionDeployment.getFunction();

        Function function = new Function();

        function.setId(dbFunction.getId());

        function.setFilePath(Paths.get(dbFunction.getFilePath()));

        function.setName(dbFunction.getName());
        function.setMemory(dbFunctionDeployment.getMemory());
        function.setTimeoutSecs(dbFunctionDeployment.getTimeoutSecs());
        function.setHandler(dbFunctionDeployment.getHandler());

        Provider provider = Provider.valueOf(dbFunctionDeployment.getProvider());

        switch (provider) {
            case AWS -> {
                function.setRegion(RegionAWS.valueOf(dbFunctionDeployment.getRegion()));
                function.setRuntime(RuntimeAWS.valueOf(dbFunctionDeployment.getRuntime()));
            }
            case GCP -> {
                function.setRegion(RegionGCP.valueOf(dbFunctionDeployment.getRegion()));
                function.setRuntime(RuntimeGCP.valueOf(dbFunctionDeployment.getRuntime()));
            } default -> throw new HolisticFaaSException("Provider not supported");
        }

        return function;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public RegionInterface getRegion() {
        return region;
    }

    public void setRegion(RegionInterface region) {
        this.region = region;
    }

    public RuntimeInterface getRuntime() {
        return runtime;
    }

    public void setRuntime(RuntimeInterface runtime) {
        this.runtime = runtime;
    }

}
