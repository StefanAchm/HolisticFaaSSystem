package com.asi.hms.enums;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.UserAWS;
import com.asi.hms.model.UserGCP;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;

import java.util.List;

class RegionFinderTest {

    private static final Logger logger = LoggerFactory.getLogger(RegionFinderTest.class);

    @Test
    void testGetAWSRegions() throws HolisticFaaSException {

        logger.info("Testing getAWSRegions");

        UserAWS user = UserAWS.fromResources("aws.properties");

        List<Region> awsRegions = RegionAWS.findAllCloudFunctionRegions(user);

        for (Region region : awsRegions) {
            logger.info("Region: " + region);
        }

    }

    @Test
    void testGetGCPRegions() throws HolisticFaaSException {

        logger.info("Testing getGCPRegions");

        UserGCP user = UserGCP.fromResources("meedesoro.json");

        List<String> gcpRegions = RegionGCP.findAllCloudFunctionRegions(user);

        for (String region : gcpRegions) {
            logger.info("Region: " + region);
        }

    }

}
