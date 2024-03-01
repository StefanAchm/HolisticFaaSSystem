package com.asi.hms.service;

import com.asi.hms.deployer.DeployAws;
import com.asi.hms.deployer.DeployInterface;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.DeployFunction;
import com.asi.hms.model.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class DeployService {

    private final Environment environment;

    @Autowired
    public DeployService(Environment environment) {
        this.environment = environment;
    }

    public boolean deploy(DeployFunction deployFunction) throws HolisticFaaSException {

//        // TODO: support other cloud providers
//        // deployFunction.getProvider()
//
//        DeployInterface deployInterface = new DeployAws();
//
//        Function function = new Function();
////        function.setSource(deployFunction.getSource()); // todo
//        function.setName(deployFunction.getName());
//        function.setMemory(deployFunction.getMemory());
//        function.setTimeoutSecs(deployFunction.getTimeout());
//        function.setHandler(deployFunction.getHandler());
//        function.setRegion(deployFunction.getRegion());
//        function.setRuntime(deployFunction.getRuntime());
//
//        Role role = new Role();
//
//
//        User user = new User();
//        user.setAccessKeyId(environment.getProperty("aws.accessKeyId"));
//        user.setSecretAccessKey(environment.getProperty("aws.secretAccessKey"));
//        user.setR(deployFunction.getArn());
//
//        return deployInterface.deployFunction(function, user);

        return false;

    }

}
