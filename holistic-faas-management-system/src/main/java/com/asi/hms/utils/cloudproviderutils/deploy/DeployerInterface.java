package com.asi.hms.utils.cloudproviderutils.deploy;

import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.utils.ProgressHandler;

public interface DeployerInterface {

    String deployFunction(Function function, ProgressHandler progressHandler);

    void updateFunction(Function function, ProgressHandler progressHandler);

}
