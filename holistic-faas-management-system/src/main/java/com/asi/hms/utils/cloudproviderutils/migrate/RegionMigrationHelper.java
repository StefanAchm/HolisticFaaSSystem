package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.utils.cloudproviderutils.enums.RegionInterface;

public class RegionMigrationHelper {

    private RegionMigrationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static APIMigrationObject fromTo(RegionInterface source, Class<? extends RegionInterface> target) {

        String city = source.getRegionGlobal().getCity();

        for (RegionInterface targetRegion : target.getEnumConstants()) {
            if(targetRegion.getRegionGlobal().getCity().equals(city)) {
                return new APIMigrationObject(source.getRegionCode(), targetRegion.getRegionCode());
            }
        }

        return new APIMigrationObject(source.getRegionCode(), null);

    }

}
