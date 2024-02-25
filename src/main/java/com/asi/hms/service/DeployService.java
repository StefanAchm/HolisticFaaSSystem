package com.asi.hms.service;

import com.asi.hms.deployer.DeployAws;
import com.asi.hms.deployer.DeployInterface;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.DeployFunction;
import com.asi.hms.model.Function;
import com.asi.hms.model.Role;
import org.springframework.stereotype.Service;

@Service
public class DeployService {

    public boolean deploy(DeployFunction deployFunction) throws HolisticFaaSException {

        // TODO: support other cloud providers

        DeployInterface deployInterface = new DeployAws();

        Function function = new Function();
        function.setSource(deployFunction.getSource());
        function.setName(deployFunction.getName());
        function.setMemory(deployFunction.getMemory());
        function.setTimeout(deployFunction.getTimeout());
        function.setProvider(deployFunction.getProvider());
        function.setHandler(deployFunction.getHandler());
        function.setRegion(deployFunction.getRegion());
        function.setRuntime(deployFunction.getRuntime());

        Role role = new Role();
        role.setArn(deployFunction.getArn());

        return deployInterface.deployFunction(function, role);

    }

}
