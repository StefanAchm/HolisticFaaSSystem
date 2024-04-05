package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.*;
import com.asi.hms.utils.cloudproviderutils.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigrationProvider implements MigrationInterface {

    private static final Logger logger = LoggerFactory.getLogger(MigrationProvider.class);

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

            APIMigrationObject regionMigration = RegionMigrationHelper.fromTo(functionDeployment.getProvider().getRegionFromCode(functionDeployment.getRegion()), targetProvider.getRegionClass());

            logger.debug("Region migration: " + regionMigration.getSource() + " -> " + regionMigration.getTarget());

            APIMigrationObject runtimeMigration = RuntimeMigrationHelper.fromTo(functionDeployment.getProvider().getRuntimeFromCode(functionDeployment.getRuntime()), targetProvider.getRuntimeClass());

            logger.debug("Runtime migration: " + runtimeMigration.getSource() + " -> " + runtimeMigration.getTarget());

            // TODO: update user credentials ????
            // Maybe not needed, if just the user is stored, but not the credentials

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
