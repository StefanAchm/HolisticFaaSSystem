package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.*;

import java.util.List;
import java.util.UUID;

public class MigrationImplUser implements MigrationInterface {

    private final List<APIUser> users;

    public MigrationImplUser(List<APIUser> users) {
        this.users = users;
    }

    @Override
    public APIMigration prepareMigration(APIMigrationPreparation migration) {

        APIMigration apiMigration = new APIMigration();

        String target = migration.getTarget();
        UUID targetUUID = UUID.fromString(target);

        APIUser targetUser = this.users.stream()
                .filter(user -> user.getId().equals(targetUUID))
                .findFirst()
                .orElseThrow(() -> new HolisticFaaSException("User not found"));

        for (APIFunction apiFunction : migration.getFunctions()) {

            String sourceUser = apiFunction.getFunctionDeployment().getUserId().toString();

            apiFunction.getFunctionDeployment().setUserId(UUID.fromString(migration.getTarget()));
            apiFunction.getFunctionDeployment().setUserName(targetUser.getUsername());

            apiMigration.getUserMigrations().add(new APIMigrationObject(sourceUser, migration.getTarget()));
            apiMigration.getFunctions().add(apiFunction);

        }

        return apiMigration;

    }

}
