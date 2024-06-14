package com.asi.hms.enums;

public enum DeployStatus {

    CREATED,

    CHANGED,

    WAITING,

    STARTED,

    FAILED,

    DEPLOYED;

    public boolean canUpdate() {
        return this == CHANGED || this == DEPLOYED;
    }


}
