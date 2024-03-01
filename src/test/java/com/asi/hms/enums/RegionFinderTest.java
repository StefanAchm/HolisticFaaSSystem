package com.asi.hms.enums;

import com.asi.hms.model.UserAWS;
import com.asi.hms.model.UserGCP;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;

import java.util.List;

@Disabled
class RegionFinderTest {

    private static final Logger logger = LoggerFactory.getLogger(RegionFinderTest.class);

    @Test
    void testGetAWSRegions() {

        UserAWS user = UserAWS.fromResources("auth/stefan01/aws.properties");

        List<Region> awsRegions = RegionAWS.findAllCloudFunctionRegions(user);

        for (Region region : awsRegions) {
            logger.info("Region: " + region);
        }

    }

    @Test
    void testGetGCPRegions() {

        UserGCP user = UserGCP.fromResources("auth/stefan01/meedesoro.json");

        List<String> gcpRegions = RegionGCP.findAllCloudFunctionRegions(user);

        for (String region : gcpRegions) {
            logger.info("Region: " + region);
        }

    }

}
