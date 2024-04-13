package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.enums.Provider;
import com.asi.hms.model.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigrationImplProvider implements MigrationInterface {

    @Override
    public APIMigration prepareMigration(APIMigrationPreparation migration) {

        Provider targetProvider = Provider.valueOf(migration.getTarget());

        APIMigration apiMigration = new APIMigration();

        for (APIFunction apiFunction : migration.getFunctions()) {

            APIFunctionDeployment functionDeployment = apiFunction.getFunctionDeployment();

            APIMigrationObject regionMigration = RegionMigrationHelper.fromTo(
                    functionDeployment.getProvider().getRegionFromCode(functionDeployment.getRegion()),
                    targetProvider.getRegionClass()
            );

            APIMigrationObject runtimeMigration = RuntimeMigrationHelper.fromTo(
                    functionDeployment.getProvider().getRuntimeFromCode(functionDeployment.getRuntime()),
                    targetProvider.getRuntimeClass()
            );

            functionDeployment.setProvider(targetProvider);
            functionDeployment.setRegion(regionMigration.getTarget());
            functionDeployment.setRuntime(runtimeMigration.getTarget());

            apiMigration.getRegionMigrations().add(regionMigration);
            apiMigration.getRuntimeMigrations().add(runtimeMigration);
            apiMigration.getFunctions().add(apiFunction);

        }

        return apiMigration;


    }

}
