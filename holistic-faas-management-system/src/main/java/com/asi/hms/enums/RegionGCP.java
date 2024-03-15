package com.asi.hms.enums;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.UserGCP;
import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.functions.v1.CloudFunctionsServiceClient;
import com.google.cloud.functions.v1.CloudFunctionsServiceSettings;
import com.google.cloud.location.ListLocationsRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//GCP: https://cloud.google.com/compute/docs/regions-zones/viewing-regions-zones?hl=de#rest

// For CloudFunctionsServiceClient:
// Region: us-central1
// Region: us-east1
// Region: us-east4
// Region: us-west1
// Region: us-west2
// Region: us-west3
// Region: us-west4
// Region: europe-central2
// Region: europe-west1
// Region: europe-west2
// Region: europe-west3
// Region: europe-west6
// Region: asia-east1
// Region: asia-east2
// Region: asia-northeast1
// Region: asia-northeast2
// Region: asia-northeast3
// Region: asia-south1
// Region: asia-southeast1
// Region: asia-southeast2
// Region: northamerica-northeast1
// Region: southamerica-east1
// Region: australia-southeast1

public enum RegionGCP implements RegionInterface {

    US_CENTRAL1("us-central1"),
    US_EAST1("us-east1"),
    US_EAST4("us-east4"),
    US_WEST1("us-west1"),
    US_WEST2("us-west2"),
    US_WEST3("us-west3"),
    US_WEST4("us-west4"),
    EUROPE_CENTRAL2("europe-central2"),
    EUROPE_WEST1("europe-west1"),
    EUROPE_WEST2("europe-west2"),
    EUROPE_WEST3("europe-west3"),
    EUROPE_WEST6("europe-west6"),
    ASIA_EAST1("asia-east1"),
    ASIA_EAST2("asia-east2"),
    ASIA_NORTHEAST1("asia-northeast1"),
    ASIA_NORTHEAST2("asia-northeast2"),
    ASIA_NORTHEAST3("asia-northeast3"),
    ASIA_SOUTH1("asia-south1"),
    ASIA_SOUTHEAST1("asia-southeast1"),
    ASIA_SOUTHEAST2("asia-southeast2"),
    NORTHAMERICA_NORTHEAST1("northamerica-northeast1"),
    SOUTHAMERICA_EAST1("southamerica-east1"),
    AUSTRALIA_SOUTHEAST1("australia-southeast1");


    private final String regionString;

    RegionGCP(String regionString) {
        this.regionString = regionString;
    }

    @Override
    public String getRegionName() {
        return regionString;
    }

    public static List<String> findAllCloudFunctionRegions(UserGCP user) {

        List<String> regions = new ArrayList<>();

        CloudFunctionsServiceSettings cloudFunctionsServiceSettings;

        try {

            cloudFunctionsServiceSettings = CloudFunctionsServiceSettings.newBuilder()
                    .setCredentialsProvider(user::getGoogleCredentials)
                    .build();

        } catch (IOException e) {

            throw new HolisticFaaSException(e.getMessage());

        }

        try (CloudFunctionsServiceClient client = CloudFunctionsServiceClient.create(cloudFunctionsServiceSettings)) {

            ListLocationsRequest request = ListLocationsRequest.newBuilder()
                    .setName("projects/" + user.getProjectName())
                    .build();

            client.listLocations(request).iterateAll()
                    .forEach(location -> regions.add(location.getLocationId()));

        } catch (InvalidArgumentException e) {

            throw new HolisticFaaSException(e.getErrorDetails().toString(), e);

        } catch (Exception e) {

            throw new HolisticFaaSException(e.getMessage());

        }

        return regions;

    }

}