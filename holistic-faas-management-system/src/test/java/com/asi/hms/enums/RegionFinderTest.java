//package com.asi.hms.enums;
//
//import com.asi.hms.model.UserAWS;
//import com.asi.hms.model.UserGCP;
//import com.asi.hms.utils.FileUtil;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import software.amazon.awssdk.regions.Region;
//
//import java.io.ByteArrayInputStream;
//import java.nio.file.Path;
//import java.util.List;
//
//@Disabled
//class RegionFinderTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(RegionFinderTest.class);
//
//    @Test
//    void testGetAWSRegions() {
//
//        Path filePath = FileUtil.getFilePathFromResourcesFile("auth/stefan01/aws.properties");
//
//        UserAWS user = UserAWS.fromInputStream(filePath);
//
//        List<Region> awsRegions = RegionAWS.findAllCloudFunctionRegions(user);
//
//        for (Region region : awsRegions) {
//            logger.info("Region: " + region);
//        }
//
//    }
//
//    @Test
//    void testGetGCPRegions() {
//
//        Path filePath = FileUtil.getFilePathFromResourcesFile("auth/stefan01/gcp.json");
//
//        UserGCP user = UserGCP.fromFile(filePath);
//
//        List<String> gcpRegions = RegionGCP.findAllCloudFunctionRegions(user);
//
//        for (String region : gcpRegions) {
//            logger.info("Region: " + region);
//        }
//
//    }
//
//}
