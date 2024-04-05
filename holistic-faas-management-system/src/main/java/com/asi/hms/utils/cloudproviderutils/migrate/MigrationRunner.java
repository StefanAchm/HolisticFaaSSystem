package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIMigration;
import com.asi.hms.model.api.APIPreparedMigration;
import com.asi.hms.utils.cloudproviderutils.enums.MigrationType;

public class MigrationRunner {

    private MigrationInterface migrationInterface;

    public MigrationRunner() {}

    public static MigrationRunner getMigrationRunner(MigrationType migrationType) {

        MigrationRunner migrationRunner = new MigrationRunner();

        switch (migrationType) {
            case FUNCTION_PROVIDER -> migrationRunner.setMigrationInterface(new MigrationProvider());
            default -> throw new HolisticFaaSException("Invalid migration type: " + migrationType);
        }

        return migrationRunner;
    }

    public MigrationInterface getMigrationInterface() {
        return migrationInterface;
    }

    public void setMigrationInterface(MigrationInterface migrationInterface) {
        this.migrationInterface = migrationInterface;
    }

    public APIPreparedMigration prepareMigration(APIMigration apiMigration) {
        return migrationInterface.prepareMigration(apiMigration);
    }

    public void migrate(APIPreparedMigration preparedMigration) {
        migrationInterface.migrate(preparedMigration);
    }

}
