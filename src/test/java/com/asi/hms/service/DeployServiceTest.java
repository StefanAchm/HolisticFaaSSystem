package com.asi.hms.service;

import com.asi.hms.deployer.DeployAws;
import com.asi.hms.enums.Region;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.Role;
import com.asi.hms.model.User;
import com.asi.hms.utils.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.UUID;


class DeployServiceTest {

    @Test
    void testDeployFunction() throws HolisticFaaSException {

        DeployAws deployAws = new DeployAws();

        Function function = new Function();
        function.setSource(FileUtil.getFilePathFromResourcesFile("zips/HolisticFaasSampleGenerator-1.0-SNAPSHOT.zip"));
        function.setName("HelloWorld-" + UUID.randomUUID()); // To avoid name conflict (update)
        function.setMemory(128);
        function.setTimeout(3);
        function.setHandler("com.asi.hsg.HelloWorldHandler::handleRequest");
        function.setRegion(Region.EU_WEST_1);
        function.setRuntime("java17");

        Role role = new Role();
        role.setArn("arn:aws:iam::546145315132:role/HF-test-role"); // todo

        Properties properties = FileUtil.getPropertiesFromResourcesFile("config.properties");

        User user = new User();
        user.setAccessKeyId(properties.getProperty("aws.accessKeyId"));
        user.setSecretAccessKey(properties.getProperty("aws.secretAccessKey"));

        Assertions.assertTrue(deployAws.deployFunction(function, role, user));

    }

}
