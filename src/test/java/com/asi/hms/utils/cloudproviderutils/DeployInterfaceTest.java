package com.asi.hms.utils.cloudproviderutils;

import com.asi.hms.enums.RegionAWS;
import com.asi.hms.enums.RegionGCP;
import com.asi.hms.enums.RuntimeAWS;
import com.asi.hms.enums.RuntimeGCP;
import com.asi.hms.model.Function;
import com.asi.hms.model.UserAWS;
import com.asi.hms.model.UserGCP;
import com.asi.hms.utils.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;


class DeployInterfaceTest {

    @Test
    void testDeployFunctionToAWS() {

        DeployAWS deployAws = new DeployAWS();

        Function function = new Function();
        function.setFilePath(FileUtil.getFilePathFromResourcesFile("zips/aws-helloworld-java-1.0-SNAPSHOT.zip"));
        function.setName("HelloWorld-" + UUID.randomUUID()); // To avoid name conflict (update)
        function.setMemory(128);
        function.setTimeoutSecs(3);
        function.setHandler("com.asi.hsg.HelloWorldHandler::handleRequest");
        function.setRegion(RegionAWS.EU_WEST_1);
        function.setRuntime(RuntimeAWS.JAVA_17);

        UserAWS user = UserAWS.fromResources("auth/stefan01/aws.properties");

        Assertions.assertTrue(deployAws.deployFunction(function, user));

    }

    @Test
    void testDeployFunctionToGCP() {

        DeployGCP deployGcp = new DeployGCP();

        Function function = new Function();
        function.setFilePath(FileUtil.getFilePathFromResourcesFile("zips/gcp-helloworld-java-1.0-SNAPSHOT.zip"));
        function.setName("HelloWorld-" + UUID.randomUUID()); // To avoid name conflict (update)
        function.setMemory(128);
        function.setTimeoutSecs(3);
        function.setHandler("com.asi.hsg.HelloWorldHandler");
        function.setRegion(RegionGCP.EUROPE_WEST1);
        function.setRuntime(RuntimeGCP.JAVA_17);

        UserGCP user = UserGCP.fromResources("auth/stefan01/gcp.json");

        Assertions.assertTrue(deployGcp.deployFunction(function, user));


    }

}
