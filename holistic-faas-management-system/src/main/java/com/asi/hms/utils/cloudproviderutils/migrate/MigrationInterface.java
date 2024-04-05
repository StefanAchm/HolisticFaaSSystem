package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigrationPreparation;
import com.asi.hms.model.api.APIMigration;

public interface MigrationInterface {

    APIMigration prepareMigration(APIMigrationPreparation migration);

}
