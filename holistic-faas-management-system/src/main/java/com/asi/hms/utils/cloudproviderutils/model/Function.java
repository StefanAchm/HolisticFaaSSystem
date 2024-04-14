package com.asi.hms.utils.cloudproviderutils.model;

import com.asi.hms.enums.Provider;
import com.asi.hms.enums.RegionInterface;
import com.asi.hms.enums.RuntimeInterface;
import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBFunctionType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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

    /**
     * The provider of the function.
     */
    private Provider provider;

    public static Function fromDbFunction(DBFunctionDeployment dbFunctionDeployment) {

        DBFunctionImplementation dbFunctionImplementation = dbFunctionDeployment.getFunctionImplementation();
        DBFunctionType dbFunctionType = dbFunctionImplementation.getFunctionType();

        Function function = new Function();

        function.setId(dbFunctionImplementation.getId());

        function.setFilePath(Paths.get(dbFunctionImplementation.getFilePath()));

        // TODO: setting a unique name for now, not sure if that is wanted!
        function.setName(dbFunctionType.getName() + "-" + dbFunctionDeployment.getId());

        function.setMemory(dbFunctionDeployment.getMemory());
        function.setTimeoutSecs(dbFunctionDeployment.getTimeoutSecs());
        function.setHandler(dbFunctionDeployment.getHandler());

        Provider provider = Provider.valueOf(dbFunctionDeployment.getProvider());

        function.setRegion(provider.getRegionFromCode(dbFunctionDeployment.getRegion()));
        function.setRuntime(provider.getRuntimeFromCode(dbFunctionDeployment.getRuntime()));
        function.setProvider(provider);

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

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

}
