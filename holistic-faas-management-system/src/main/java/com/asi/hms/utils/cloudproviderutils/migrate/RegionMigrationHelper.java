package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.model.RegionGlobal;
import com.asi.hms.enums.RegionInterface;

public class RegionMigrationHelper {

    private RegionMigrationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static APIMigrationObject fromTo(RegionInterface source, Class<? extends RegionInterface> target) {

        RegionGlobal sourceRegionGlobal = source.getRegionGlobal();

        for (RegionInterface targetRegion : target.getEnumConstants()) {
            if(targetRegion.getRegionGlobal().getCity().equals(sourceRegionGlobal.getCity())) {
                return new APIMigrationObject(source.getRegionCode(), targetRegion.getRegionCode());
            }
        }

        return new APIMigrationObject(source.getRegionCode(), null);

    }

}
