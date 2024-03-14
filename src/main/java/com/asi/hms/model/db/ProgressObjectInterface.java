package com.asi.hms.model.db;

import com.asi.hms.enums.DeployStatus;

import java.util.UUID;

public interface ProgressObjectInterface {

    UUID getId();

    DeployStatus getStatus();

    String getStatusMessage();

}
