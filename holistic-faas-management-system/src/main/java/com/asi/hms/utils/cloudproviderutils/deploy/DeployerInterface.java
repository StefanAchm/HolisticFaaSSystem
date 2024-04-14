package com.asi.hms.utils.cloudproviderutils.deploy;

import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.utils.ProgressHandler;

public interface DeployerInterface {

    boolean deployFunction(Function function, ProgressHandler progressHandler);

    boolean updateFunction(Function function, ProgressHandler progressHandler);

}
