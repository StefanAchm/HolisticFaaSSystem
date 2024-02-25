package com.asi.hms.enums;

public enum Region {

    US_EAST_1("us-east-1"),
    EU_WEST_1("eu-west-1");

    private final String regionString;

    private Region(String regionString) {
        this.regionString = regionString;
    }

    public software.amazon.awssdk.regions.Region toAwsRegion() {
        return software.amazon.awssdk.regions.Region.of(regionString);
    }



}
