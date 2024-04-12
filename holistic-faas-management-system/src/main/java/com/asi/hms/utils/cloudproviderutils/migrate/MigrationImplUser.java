package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.model.api.APIMigrationPreparation;

import java.util.UUID;

public class MigrationImplUser implements MigrationInterface {

    @Override
    public APIMigration prepareMigration(APIMigrationPreparation migration) {

        APIMigration apiMigration = new APIMigration();

        for (APIFunction apiFunction : migration.getFunctions()) {

            String sourceUser = apiFunction.getFunctionDeployment().getUserId().toString();

            apiFunction.getFunctionDeployment().setUserId(UUID.fromString(migration.getTarget()));

            apiMigration.getUserMigrations().add(new APIMigrationObject(sourceUser, migration.getTarget()));
            apiMigration.getFunctions().add(apiFunction);

        }

        return apiMigration;

    }

}
