package com.asi.hms.deployer;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.Role;
import com.asi.hms.model.User;

public interface DeployInterface {

    boolean deployFunction(Function function, Role role, User user) throws HolisticFaaSException;

}
