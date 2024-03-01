package com.asi.hms.deployer;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.UserInterface;

public interface DeployInterface<U extends UserInterface> {

    boolean deployFunction(Function function, U user) throws HolisticFaaSException;

}
