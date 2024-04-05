package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigration;
import com.asi.hms.model.api.APIPreparedMigration;

public interface MigrationInterface {

    APIPreparedMigration prepareMigration(APIMigration migration);

    void migrate(APIPreparedMigration preparedMigration);

}
