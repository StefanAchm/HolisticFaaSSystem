package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.enums.*;
import com.asi.hms.model.api.APIMigrationObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RegionMigrationHelperTest {

    private static Stream<Arguments> fromTo() {

        // TODO: Currently just mapped by city name (e.g. "eu-west-2" to "europe-west2")

        return Stream.of(

                // AWS -> GCP
                Arguments.of(RegionAWS.EU_WEST_2, RegionGCP.EUROPE_WEST2, RegionGCP.class),
                Arguments.of(RegionAWS.EU_CENTRAL_1, RegionGCP.EUROPE_WEST3, RegionGCP.class),
                Arguments.of(RegionAWS.AP_SOUTH_1, RegionGCP.ASIA_SOUTH1, RegionGCP.class),
                Arguments.of(RegionAWS.AP_SOUTHEAST_2, RegionGCP.AUSTRALIA_SOUTHEAST1, RegionGCP.class),
                Arguments.of(RegionAWS.AP_NORTHEAST_3, RegionGCP.ASIA_NORTHEAST2, RegionGCP.class),
                Arguments.of(RegionAWS.AP_NORTHEAST_2, RegionGCP.ASIA_NORTHEAST3, RegionGCP.class),
                Arguments.of(RegionAWS.AP_NORTHEAST_1, RegionGCP.ASIA_NORTHEAST1, RegionGCP.class),

                // GCP -> AWS
                Arguments.of(RegionGCP.EUROPE_WEST2, RegionAWS.EU_WEST_2, RegionAWS.class),
                Arguments.of(RegionGCP.EUROPE_WEST3, RegionAWS.EU_CENTRAL_1, RegionAWS.class),
                Arguments.of(RegionGCP.ASIA_NORTHEAST1, RegionAWS.AP_NORTHEAST_1, RegionAWS.class),
                Arguments.of(RegionGCP.ASIA_NORTHEAST2, RegionAWS.AP_NORTHEAST_3, RegionAWS.class),
                Arguments.of(RegionGCP.ASIA_NORTHEAST3, RegionAWS.AP_NORTHEAST_2, RegionAWS.class),
                Arguments.of(RegionGCP.ASIA_SOUTH1, RegionAWS.AP_SOUTH_1, RegionAWS.class),
                Arguments.of(RegionGCP.AUSTRALIA_SOUTHEAST1, RegionAWS.AP_SOUTHEAST_2, RegionAWS.class)

        );

    }

    @ParameterizedTest
    @MethodSource("fromTo")
    void fromTo(RegionInterface source, RegionInterface target, Class<? extends RegionInterface> targetClass) {

        APIMigrationObject apiMigrationObject = RegionMigrationHelper.fromTo(source, targetClass);

        Assertions.assertEquals(target != null ? target.getRegionCode() : null, apiMigrationObject.getTarget());

    }

}