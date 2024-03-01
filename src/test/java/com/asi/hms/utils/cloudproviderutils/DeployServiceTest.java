package com.asi.hms.utils.cloudproviderutils;

import com.asi.hms.enums.RegionAWS;
import com.asi.hms.enums.RegionGCP;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.UserAWS;
import com.asi.hms.model.UserGCP;
import com.asi.hms.utils.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;


class DeployServiceTest {

    @Test
    void testDeployFunctionToAWS() throws HolisticFaaSException {

        DeployAws deployAws = new DeployAws();

        Function function = new Function();
        function.setFilePath(FileUtil.getFilePathFromResourcesFile("zips/aws-helloworld-java-1.0-SNAPSHOT.zip"));
        function.setName("HelloWorld-" + UUID.randomUUID()); // To avoid name conflict (update)
        function.setMemory(128);
        function.setTimeoutSecs(3);
        function.setHandler("com.asi.hsg.HelloWorldHandler::handleRequest");
        function.setRegionInterface(RegionAWS.EU_WEST_1);
        function.setRuntime("java17");

        UserAWS user = UserAWS.fromResources("aws.properties");

        Assertions.assertTrue(deployAws.deployFunction(function, user));

    }

    @Test
    void testDeployFunctionToGCP() throws HolisticFaaSException {

        DeployGcp deployGcp = new DeployGcp();

        Function function = new Function();
        function.setFilePath(FileUtil.getFilePathFromResourcesFile("zips/gcp-helloworld-java-1.0-SNAPSHOT.zip"));
        function.setName("HelloWorld-" + UUID.randomUUID()); // To avoid name conflict (update)
        function.setMemory(128);
        function.setTimeoutSecs(3);
        function.setHandler("com.asi.hsg.HelloWorldHandler");
        function.setRegionInterface(RegionGCP.EUROPE_WEST1);
        function.setRuntime("java17");

        UserGCP user = UserGCP.fromResources("meedesoro.json");

        Assertions.assertTrue(deployGcp.deployFunction(function, user));


    }

}
