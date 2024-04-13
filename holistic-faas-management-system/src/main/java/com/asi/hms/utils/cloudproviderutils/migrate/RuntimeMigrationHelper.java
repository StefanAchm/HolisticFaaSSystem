package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.model.RuntimeGlobal;
import com.asi.hms.enums.RuntimeInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeMigrationHelper {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeMigrationHelper.class);

    private RuntimeMigrationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static APIMigrationObject fromTo(RuntimeInterface source, Class<? extends RuntimeInterface> target) {

        RuntimeGlobal sourceRuntimeGlobal = source.getRuntimeGlobal();

        for (RuntimeInterface targetRuntime : target.getEnumConstants()) {

            RuntimeGlobal targetRuntimeGlobal = targetRuntime.getRuntimeGlobal();

            if(sourceRuntimeGlobal.getLanguage().equals(targetRuntimeGlobal.getLanguage())
                    && sourceRuntimeGlobal.getVersion().equals(targetRuntimeGlobal.getVersion())) {

                logger.debug("Runtime migration: {} -> {} ", source.getRuntimeCode(), targetRuntime.getRuntimeCode());
                return new APIMigrationObject(source.getRuntimeCode(), targetRuntime.getRuntimeCode());

            }


        }

        logger.warn("Runtime migration: {} -> null", source.getRuntimeCode());
        return new APIMigrationObject(source.getRuntimeCode(), null);

    }

}
