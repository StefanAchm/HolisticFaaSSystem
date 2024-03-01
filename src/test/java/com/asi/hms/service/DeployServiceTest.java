package com.asi.hms.service;

import com.asi.hms.enums.Provider;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.DeployFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DeployServiceTest {

    @Test
    void testDeployService() throws HolisticFaaSException {

        // {
        //  "filePath": "zips/aws-helloworld-java-1.0-SNAPSHOT.zip",
        //  "handler": "com.asi.hsg.HelloWorldHandler::handleRequest",
        //  "memory": 128,
        //  "name": "HelloWorld-FromAPI-01",
        //  "provider": "AWS",
        //  "region": "US_WEST_1",
        //  "runtime": "JAVA_17",
        //  "timeoutSecs": 3
        //}

        DeployFunction deployFunction = new DeployFunction();
        deployFunction.setFilePath("zips/aws-helloworld-java-1.0-SNAPSHOT.zip");
        deployFunction.setHandler("com.asi.hsg.HelloWorldHandler::handleRequest");
        deployFunction.setMemory(128);
        deployFunction.setName("HelloWorld-FromAPI-01");
        deployFunction.setProvider(Provider.AWS);
        deployFunction.setRegion("US_WEST_1"); // Exact enum!
        deployFunction.setRuntime("JAVA_17"); // Exact enum!
        deployFunction.setTimeoutSecs(3);

        DeployService deployService = new DeployService();

        Assertions.assertTrue(deployService.deploy(deployFunction));

    }

    @Test
    void testDeployGCP() throws HolisticFaaSException {

        // {
        //  "filePath": "zips/gcp-helloworld-java-1.0-SNAPSHOT.zip",
        //  "handler": "com.asi.hsg.HelloWorldHandler",
        //  "memory": 128,
        //  "name": "HelloWorld-FromAPI-01",
        //  "provider": "GCP",
        //  "region": "US_WEST1",
        //  "runtime": "JAVA_17",
        //  "timeoutSecs": 3
        //}

        DeployFunction deployFunction = new DeployFunction();
        deployFunction.setFilePath("zips/gcp-helloworld-java-1.0-SNAPSHOT.zip");
        deployFunction.setHandler("com.asi.hsg.HelloWorldHandler");
        deployFunction.setMemory(128);
        deployFunction.setName("HelloWorld-FromAPI-01");
        deployFunction.setProvider(Provider.GCP);
        deployFunction.setRegion("US_WEST1"); // Exact enum!
        deployFunction.setRuntime("JAVA_17"); // Exact enum!
        deployFunction.setTimeoutSecs(3);

        DeployService deployService = new DeployService();

        Assertions.assertTrue(deployService.deploy(deployFunction));

    }

}
