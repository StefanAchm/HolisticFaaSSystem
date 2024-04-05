package com.asi.hms.utils.cloudproviderutils.enums;

public enum Provider {

    AWS,

    GCP;

    public RegionInterface getRegionFromCode(String region) {
        return switch (this) {
            case AWS -> RegionAWS.fromCode(region);
            case GCP -> RegionGCP.fromCode(region);
        };
    }

    public RuntimeInterface getRuntimeFromCode(String runtime) {
        return switch (this) {
            case AWS -> RuntimeAWS.fromCode(runtime);
            case GCP -> RuntimeGCP.fromCode(runtime);
        };
    }

    public Class<? extends RegionInterface> getRegionClass() {
        return switch (this) {
            case AWS -> RegionAWS.class;
            case GCP -> RegionGCP.class;
        };
    }

    public Class<? extends RuntimeInterface> getRuntimeClass() {
        return switch (this) {
            case AWS -> RuntimeAWS.class;
            case GCP -> RuntimeGCP.class;
        };
    }

}
