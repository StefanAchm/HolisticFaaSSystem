package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIFunctionFlat;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.model.api.APIMigrationPreparation;

public class MigrationImplRegion implements MigrationInterface {

    @Override
    public APIMigration prepareMigration(APIMigrationPreparation migration) {

        String targetRegion = migration.getTarget();

        APIMigration apiMigration = new APIMigration();

        for (APIFunctionFlat apiFunctionFlat : migration.getFunctions()) {

            String sourceRegion = apiFunctionFlat.getFunctionDeployment().getRegion();

            apiFunctionFlat.getFunctionDeployment().setRegion(targetRegion);

            apiMigration.getRegionMigrations().add(new APIMigrationObject(sourceRegion, targetRegion));
            apiMigration.getFunctions().add(apiFunctionFlat);

        }

        return apiMigration;

    }

}
