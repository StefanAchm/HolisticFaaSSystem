package com.asi.hms.enums;

// TODO: support AWS and GCP
public enum Region {

    US_EAST_1("us-east-1"), // gcp: (us-east1)
    EU_WEST_1("eu-west-1"); // gcp: (europe-west1)

    private final String regionString;

    Region(String regionString) {
        this.regionString = regionString;
    }

    public software.amazon.awssdk.regions.Region toAwsRegion() {
        return software.amazon.awssdk.regions.Region.of(regionString);
    }


    public String toGcpRegion() {

        return switch (this) {
            case US_EAST_1 -> "us-east1";
            case EU_WEST_1 -> "europe-west1";
            default -> throw new IllegalArgumentException("Invalid region: " + this);
        };

    }

}
