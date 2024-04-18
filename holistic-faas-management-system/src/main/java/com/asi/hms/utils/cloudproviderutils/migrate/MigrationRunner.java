package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigrationPreparation;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.enums.MigrationType;
import com.asi.hms.model.api.APIUser;

import java.util.List;

public class MigrationRunner {

    private MigrationInterface migrationInterface;

    private MigrationRunner() {
    }

    public static MigrationRunner getMigrationRunner(MigrationType migrationType, List<APIUser> users) {

        MigrationRunner migrationRunner = new MigrationRunner();

        migrationRunner.migrationInterface =
                switch (migrationType) {
                    case FUNCTION_PROVIDER -> new MigrationImplProvider();
                    case FUNCTION_REGION -> new MigrationImplRegion();
                    case FUNCTION_USER -> new MigrationImplUser(users);
                    case FUNCTION_CUSTOM -> new MigrationImplCustom();
                };

        return migrationRunner;

    }

    public APIMigration prepareMigration(APIMigrationPreparation apiMigrationPreparation) {
        return migrationInterface.prepareMigration(apiMigrationPreparation);
    }

}
