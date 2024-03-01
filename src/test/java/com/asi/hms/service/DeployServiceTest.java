package com.asi.hms.service;

import com.asi.hms.enums.Provider;
import com.asi.hms.model.api.APIFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DeployServiceTest {

    @Test
    void testDeployService() {

        // {
        //  "filePath": "zips/aws-helloworld-java-1.0-SNAPSHOT.zip",
        //  "handler": "com.asi.hsg.HelloWorldHandler::handleRequest",
        //  "memory": 128,
        //  "name": "HelloWorld-FromAPI-01",
        //  "provider": "AWS",
        //  "region": "US_WEST_1",
        //  "runtime": "JAVA_17",
        //  "timeoutSecs": 3,
        //  "user": "auth/stefan01/aws.properties"
        //}

        APIFunction APIFunction = new APIFunction();
        APIFunction.setFilePath("zips/aws-helloworld-java-1.0-SNAPSHOT.zip");
        APIFunction.setHandler("com.asi.hsg.HelloWorldHandler::handleRequest");
        APIFunction.setMemory(128);
        APIFunction.setName("HelloWorld-FromAPI-01");
        APIFunction.setProvider(Provider.AWS);
        APIFunction.setRegion("US_WEST_1"); // Exact enum!
        APIFunction.setRuntime("JAVA_17"); // Exact enum!
        APIFunction.setTimeoutSecs(3);
        APIFunction.setUser("stefan01");

        DeployService deployService = new DeployService();

        Assertions.assertTrue(deployService.deploy(APIFunction));

    }

    @Test
    void testDeployGCP() {

        // {
        //  "filePath": "zips/gcp-helloworld-java-1.0-SNAPSHOT.zip",
        //  "handler": "com.asi.hsg.HelloWorldHandler",
        //  "memory": 128,
        //  "name": "HelloWorld-FromAPI-01",
        //  "provider": "GCP",
        //  "region": "US_WEST1",
        //  "runtime": "JAVA_17",
        //  "timeoutSecs": 3,
        //  "user": "auth/stefan01/gcp.json"
        //}

        APIFunction APIFunction = new APIFunction();
        APIFunction.setFilePath("zips/gcp-helloworld-java-1.0-SNAPSHOT.zip");
        APIFunction.setHandler("com.asi.hsg.HelloWorldHandler");
        APIFunction.setMemory(128);
        APIFunction.setName("HelloWorld-FromAPI-01");
        APIFunction.setProvider(Provider.GCP);
        APIFunction.setRegion("US_WEST1"); // Exact enum!
        APIFunction.setRuntime("JAVA_17"); // Exact enum!
        APIFunction.setTimeoutSecs(3);
        APIFunction.setUser("stefan01");

        DeployService deployService = new DeployService();

        Assertions.assertTrue(deployService.deploy(APIFunction));

    }

}
