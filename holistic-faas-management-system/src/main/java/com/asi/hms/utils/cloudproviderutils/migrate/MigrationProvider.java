package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.*;
import com.asi.hms.utils.cloudproviderutils.enums.*;

public class MigrationProvider implements MigrationInterface {

    @Override
    public APIMigration prepareMigration(APIMigrationPreparation migration) {

        Provider targetProvider;

        try {
            targetProvider = Provider.valueOf(migration.getTarget());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid provider: " + migration.getTarget());
        }

        APIMigration apiMigration = new APIMigration();

        for (APIFunction apiFunction : migration.getFunctions()) {

            APIFunctionDeployment functionDeployment = apiFunction.getFunctionDeployment();

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

            functionDeployment.setProvider(targetProvider);
            functionDeployment.setRegion(regionMigration.getTarget());
            functionDeployment.setRuntime(runtimeMigration.getTarget());

            apiMigration.getRegionMigrations().add(regionMigration);
            apiMigration.getRuntimeMigrations().add(runtimeMigration);
            apiMigration.getFunctions().add(apiFunction);

        }

        return apiMigration;


    }

    @Override
    public void migrate(APIMigration preparedMigration) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
