package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIMigrationPreparation;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.utils.cloudproviderutils.enums.MigrationType;

public class MigrationRunner {

    private MigrationInterface migrationInterface;

    private MigrationRunner() {
    }

    public static MigrationRunner getMigrationRunner(MigrationType migrationType) {

        MigrationRunner migrationRunner = new MigrationRunner();

        switch (migrationType) {
            case FUNCTION_PROVIDER -> migrationRunner.setMigrationInterface(new MigrationProvider());
            case FUNCTION_REGION -> migrationRunner.setMigrationInterface(new MigrationRegion());
            default -> throw new HolisticFaaSException("Invalid migration type: " + migrationType);
        }

        return migrationRunner;
    }

    private void setMigrationInterface(MigrationInterface migrationInterface) {
        this.migrationInterface = migrationInterface;
    }

    public APIMigration prepareMigration(APIMigrationPreparation apiMigrationPreparation) {
        return migrationInterface.prepareMigration(apiMigrationPreparation);
    }

}
