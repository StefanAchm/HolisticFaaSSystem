package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.enums.RuntimeGlobal;
import com.asi.hms.enums.RuntimeInterface;

public class RuntimeMigrationHelper {

    private RuntimeMigrationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static APIMigrationObject fromTo(RuntimeInterface source, Class<? extends RuntimeInterface> target) {

        RuntimeGlobal sourceRuntimeGlobal = source.getRuntimeGlobal();

        for (RuntimeInterface targetRuntime : target.getEnumConstants()) {

            RuntimeGlobal targetRuntimeGlobal = targetRuntime.getRuntimeGlobal();

            if(sourceRuntimeGlobal.getLanguage().equals(targetRuntimeGlobal.getLanguage()) && sourceRuntimeGlobal.getVersion().equals(targetRuntimeGlobal.getVersion())) {
                return new APIMigrationObject(source.getRuntimeCode(), targetRuntime.getRuntimeCode());
            }


        }

        return new APIMigrationObject(source.getRuntimeCode(), null);

    }

}
