package com.asi.hms.utils.cloudproviderutils;

import com.asi.hms.model.Function;
import com.asi.hms.model.UserInterface;
import com.asi.hms.utils.ProgressHandler;

public interface DeployInterface<U extends UserInterface> {

    boolean deployFunction(Function function, U user, ProgressHandler progressHandler);

}
