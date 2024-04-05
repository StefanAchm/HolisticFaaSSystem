package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.*;
import com.asi.hms.utils.cloudproviderutils.enums.*;

public class MigrationProvider implements MigrationInterface {

    @Override
    public APIPreparedMigration prepareMigration(APIMigration migration) {

        Provider targetProvider;

        try {
            targetProvider = Provider.valueOf(migration.getTarget());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid provider: " + migration.getTarget());
        }

        APIPreparedMigration apiPreparedMigration = new APIPreparedMigration();

        for (APIFunction apiFunction : migration.getFunctions()) {

            APIFunctionDeployment functionDeployment = apiFunction.getFunctionDeployment();

            functionDeployment.setProvider(targetProvider);

            APIMigrationObject regionMigration;
            APIMigrationObject runtimeMigration;

            if(targetProvider == Provider.AWS && functionDeployment.getProvider().equals(Provider.GCP)) {

                regionMigration = RegionMigrationHelper.fromTo(RegionGCP.valueOf(functionDeployment.getRegion()), RegionAWS.class);
                runtimeMigration = RuntimeMigrationHelper.fromTo(RuntimeGCP.valueOf(functionDeployment.getRuntime()), RuntimeAWS.class);

            } else if(targetProvider == Provider.GCP && functionDeployment.getProvider().equals(Provider.AWS)) {

                regionMigration = RegionMigrationHelper.fromTo(RegionAWS.valueOf(functionDeployment.getRegion()), RegionGCP.class);
                runtimeMigration = RuntimeMigrationHelper.fromTo(RuntimeAWS.valueOf(functionDeployment.getRuntime()), RuntimeGCP.class);

            } else {

                throw new HolisticFaaSException("Invalid provider migration: " + functionDeployment.getProvider() + " to " + targetProvider);

            }

            functionDeployment.setRegion(regionMigration.getTarget());
            functionDeployment.setRuntime(runtimeMigration.getTarget());

            apiPreparedMigration.getRegionMigrations().add(regionMigration);
            apiPreparedMigration.getRuntimeMigrations().add(runtimeMigration);
            apiPreparedMigration.getFunctions().add(apiFunction);

        }

        return apiPreparedMigration;


    }

    @Override
    public void migrate(APIPreparedMigration preparedMigration) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
