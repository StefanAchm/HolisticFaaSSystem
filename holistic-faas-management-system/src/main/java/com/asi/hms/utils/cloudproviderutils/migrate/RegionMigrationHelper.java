package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.model.api.APIMigrationObject;
import com.asi.hms.model.RegionGlobal;
import com.asi.hms.enums.RegionInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegionMigrationHelper {

    private static final Logger logger = LoggerFactory.getLogger(RegionMigrationHelper.class);

    private RegionMigrationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static APIMigrationObject fromTo(RegionInterface source, Class<? extends RegionInterface> target) {

        RegionGlobal sourceRegionGlobal = source.getRegionGlobal();

        for (RegionInterface targetRegion : target.getEnumConstants()) {
            if(targetRegion.getRegionGlobal().getCity().equals(sourceRegionGlobal.getCity())) {

                logger.debug("Region migration: {} -> {}", source.getRegionCode(), targetRegion.getRegionCode());
                return new APIMigrationObject(source.getRegionCode(), targetRegion.getRegionCode());
            }
        }

        logger.warn("Region migration: {} -> null", source.getRegionCode());
        return new APIMigrationObject(source.getRegionCode(), null);

    }

}
