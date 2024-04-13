package com.asi.hms.utils.cloudproviderutils.deploy;

import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.model.UserInterface;
import com.asi.hms.utils.ProgressHandler;

public interface DeployInterface<U extends UserInterface> {

    boolean deployFunction(Function function, U user, ProgressHandler progressHandler);

}
