package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.utils.cloudproviderutils.enums.RuntimeGlobal;
import com.asi.hms.utils.cloudproviderutils.enums.RuntimeInterface;

public class RuntimeMigrationHelper {

    private RuntimeMigrationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static APIMigrationObject fromTo(RuntimeInterface source, Class<? extends RuntimeInterface> target) {

        RuntimeGlobal runtimeGlobal = source.getRuntimeGlobal();

        for (RuntimeInterface targetRuntime : target.getEnumConstants()) {

            RuntimeGlobal targetRuntimeGlobal = targetRuntime.getRuntimeGlobal();

            if(runtimeGlobal.getLanguage().equals(targetRuntimeGlobal.getLanguage()) && runtimeGlobal.getVersion().equals(targetRuntimeGlobal.getVersion())) {
                return new APIMigrationObject(source.getRuntimeCode(), targetRuntime.getRuntimeCode());
            }


        }

        return new APIMigrationObject(source.getRuntimeCode(), null);

    }

}
