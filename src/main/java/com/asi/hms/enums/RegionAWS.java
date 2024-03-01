package com.asi.hms.enums;

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

import com.asi.hms.model.UserAWS;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;

import java.util.ArrayList;
import java.util.List;

public enum RegionAWS implements RegionInterface {

    AP_SOUTH_1("ap-south-1"),
    CA_CENTRAL_1("ca-central-1"),
    EU_CENTRAL_1("eu-central-1"),
    US_WEST_1("us-west-1"),
    US_WEST_2("us-west-2"),
    EU_NORTH_1("eu-north-1"),
    EU_WEST_3("eu-west-3"),
    EU_WEST_2("eu-west-2"),
    EU_WEST_1("eu-west-1"),
    AP_NORTHEAST_3("ap-northeast-3"),
    AP_NORTHEAST_2("ap-northeast-2"),
    AP_NORTHEAST_1("ap-northeast-1"),
    SA_EAST_1("sa-east-1"),
    AP_SOUTHEAST_1("ap-southeast-1"),
    AP_SOUTHEAST_2("ap-southeast-2"),
    US_EAST_1("us-east-1"),
    US_EAST_2("us-east-2");

    private final String regionString;

    RegionAWS(String regionString) {
        this.regionString = regionString;
    }

    @Override
    public String getRegionName() {
        return regionString;
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
