package com.asi.hms.utils.cloudproviderutils;

import com.asi.hms.enums.RegionAWS;
import com.asi.hms.enums.RegionGCP;
import com.asi.hms.enums.RuntimeAWS;
import com.asi.hms.enums.RuntimeGCP;
import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.model.UserAWS;
import com.asi.hms.model.UserGCP;
import com.asi.hms.utils.FileUtil;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployAWS;
import com.asi.hms.utils.cloudproviderutils.deploy.DeployGCP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.UUID;


class DeployerInterfaceTest {

    @Test
    void testDeployFunctionToAWS() {



        Function function = new Function();
        function.setFilePath(FileUtil.getFilePathFromResourcesFile("zips/aws-helloworld-java-1.0-SNAPSHOT.zip"));
        function.setName("HelloWorld-" + UUID.randomUUID()); // To avoid name conflict (update)
        function.setMemory(128);
        function.setTimeoutSecs(3);
        function.setHandler("com.asi.hsg.HelloWorldHandler::handleRequest");
        function.setRegion(RegionAWS.EU_WEST_1);
        function.setRuntime(RuntimeAWS.JAVA_17);

        Path filePath = FileUtil.getFilePathFromResourcesFile("auth/stefan01/aws.properties");

        DeployAWS deployAws = new DeployAWS(UserAWS.fromFile(filePath));

        deployAws.deployFunction(function, null);

    }

    @Test
    void testDeployFunctionToGCP() {

        Function function = new Function();
        function.setFilePath(FileUtil.getFilePathFromResourcesFile("zips/gcp-helloworld-java-1.0-SNAPSHOT.zip"));
        function.setName("HelloWorld-" + UUID.randomUUID()); // To avoid name conflict (update)
        function.setMemory(128);
        function.setTimeoutSecs(3);
        function.setHandler("com.asi.hsg.HelloWorldHandler");
        function.setRegion(RegionGCP.EUROPE_WEST1);
        function.setRuntime(RuntimeGCP.JAVA_17);

        Path filePath = FileUtil.getFilePathFromResourcesFile("auth/stefan01/gcp.json");

        DeployGCP deployGcp = new DeployGCP(UserGCP.fromFile(filePath));

        deployGcp.deployFunction(function, null);

    }

}
