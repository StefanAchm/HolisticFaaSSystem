package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.model.api.APIMigrationPreparation;

public class MigrationRegion implements MigrationInterface {

    @Override
    public APIMigration prepareMigration(APIMigrationPreparation migration) {

        String targetRegion = migration.getTarget();

        APIMigration apiMigration = new APIMigration();

        for (APIFunction apiFunction : migration.getFunctions()) {

            String sourceRegion = apiFunction.getFunctionDeployment().getRegion();

            apiFunction.getFunctionDeployment().setRegion(targetRegion);

            apiMigration.getRegionMigrations().add(new APIMigrationObject(sourceRegion, targetRegion));
            apiMigration.getFunctions().add(apiFunction);

        }

        return apiMigration;

    }

}
