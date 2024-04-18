package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigration;
import com.asi.hms.model.api.APIMigrationPreparation;

public class MigrationImplCustom implements MigrationInterface {

    @Override
    public APIMigration prepareMigration(APIMigrationPreparation migration) {

        APIMigration apiMigration = new APIMigration();

        migration.getFunctions().forEach(apiFunction -> apiMigration.getFunctions().add(apiFunction));

        return apiMigration;

    }

}
