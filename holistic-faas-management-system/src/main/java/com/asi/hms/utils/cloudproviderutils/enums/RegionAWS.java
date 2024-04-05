package com.asi.hms.utils.cloudproviderutils.enums;

// Region: ap-south-1
// Region: ca-central-1
// Region: eu-central-1
// Region: us-west-1
// Region: us-west-2
// Region: eu-north-1
// Region: eu-west-3
// Region: eu-west-2
// Region: eu-west-1
// Region: ap-northeast-3
// Region: ap-northeast-2
// Region: ap-northeast-1
// Region: sa-east-1
// Region: ap-southeast-1
// Region: ap-southeast-2
// Region: us-east-1
// Region: us-east-2

import com.asi.hms.utils.cloudproviderutils.model.UserAWS;
import com.fasterxml.jackson.annotation.JsonGetter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;

import java.util.ArrayList;
import java.util.List;

public enum RegionAWS implements RegionInterface {

    US_EAST_1("us-east-1", "US Eeast (N. Virginia)"),
    US_EAST_2("us-east-2", "US East (Ohio)"),
    US_WEST_1("us-west-1", "US West (N. California)"),
    US_WEST_2("us-west-2", "US West (Oregon)"),

    EU_CENTRAL_1("eu-central-1", "EU (Frankfurt)"),
    EU_NORTH_1("eu-north-1", "EU (Stockholm)"),
    EU_WEST_3("eu-west-3", "EU (Paris)"),
    EU_WEST_2("eu-west-2", "EU (London)"),
    EU_WEST_1("eu-west-1", "EU (Ireland)"),



    AP_SOUTH_1("ap-south-1", "Asia Pacific (Mumbai)"),
    AP_SOUTHEAST_1("ap-southeast-1", "Asia Pacific (Singapore)"),
    AP_SOUTHEAST_2("ap-southeast-2", "Asia Pacific (Sydney)"),
    AP_NORTHEAST_3("ap-northeast-3", "Asia Pacific (Osaka)"),
    AP_NORTHEAST_2("ap-northeast-2", "Asia Pacific (Seoul)"),
    AP_NORTHEAST_1("ap-northeast-1", "Asia Pacific (Tokyo)"),

    CA_CENTRAL_1("ca-central-1", "Canada (Central)"),

    SA_EAST_1("sa-east-1", "South America (Sao Paulo)")

    ;


    private final String regionCode;
    private final String regionName;

    RegionAWS(String regionCode, String regionName) {
        this.regionCode = regionCode;
        this.regionName = regionName;
    }

    public static RegionAWS fromCode(String code) {
        for (RegionAWS region : RegionAWS.values()) {
            if (region.getRegionCode().equals(code)) {
                return region;
            }
        }
        return null;
    }

    @Override
    public String getRegionCode() {
        return regionCode;
    }

    @Override
    public String getRegionName() {
        return regionName;
    }

    @Override
    public RegionGlobal getRegionGlobal() {

        // Extract the continent and city from the region name
        // Continent is before the (
        // And city is within the ( and )

        String continent = regionName.substring(0, regionName.indexOf("(") - 1);
        String city = regionName.substring(regionName.indexOf("(") + 1, regionName.indexOf(")"));

        RegionGlobal regionGlobal = new RegionGlobal();
        regionGlobal.setContinent(continent);
        regionGlobal.setCity(city);

        return regionGlobal;

    }

    public static List<Region> findAllCloudFunctionRegions(UserAWS user) {

        List<Region> regions = new ArrayList<>();

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(user.getAccessKeyId(), user.getSecretAccessKey());

        for(Region region : Region.regions()) {

            // Try if the region is available for AWS Lambda

            try {

                LambdaClient awsLambda = LambdaClient.builder()
                        .region(region)
                        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                        .build();

                awsLambda.listFunctions();

                regions.add(region);

            } catch (Exception e) {

                // Do nothing, region not supported for AWS Lambda

            }

        }

        return regions;

    }

}
