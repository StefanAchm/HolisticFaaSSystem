package com.asi.hms.utils.cloudproviderutils.enums;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.cloudproviderutils.model.UserGCP;
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

    US_CENTRAL1("us-central1", "Council Bluffs, Iowa, North America"),

    US_EAST1("us-east1", "Moncks Corner, South Carolina, North America"),
    US_EAST4("us-east4", "Ashburn, Virginia, North America"),
    US_WEST1("us-west1", "The Dalles, Oregon, North America"),
    US_WEST2("us-west2", "Los Angeles, California, North America"),
    US_WEST3("us-west3", "Salt Lake City, Utah, North America"),
    US_WEST4("us-west4", "Las Vegas, Nevada, North America"),

    EUROPE_CENTRAL2("europe-central2", "Warsaw, Poland, Europe"),
    EUROPE_WEST1("europe-west1", "St. Ghislain, Belgium, Europe"),
    EUROPE_WEST2("europe-west2", "London, England, Europe"),
    EUROPE_WEST3("europe-west3", "Frankfurt, Germany, Europe"),
    EUROPE_WEST6("europe-west6", "Zürich, Switzerland, Europe"), // Region name consists of continent, country, and city

    // APAC = Asia Pacific
    ASIA_EAST1("asia-east1", "Changhua County, Taiwan, APAC"),
    ASIA_EAST2("asia-east2", "Hong Kong, Chine, APAC"),
    ASIA_NORTHEAST1("asia-northeast1", "Tokyo, Japan, APAC"),
    ASIA_NORTHEAST2("asia-northeast2", "Osaka, Japan, APAC"),
    ASIA_NORTHEAST3("asia-northeast3", "Seoul, South Korea, APAC"),
    ASIA_SOUTH1("asia-south1", "Mumbai, India, APAC"),
    ASIA_SOUTHEAST1("asia-southeast1", "Jurong West, Singapore, APAC"),
    ASIA_SOUTHEAST2("asia-southeast2", "Jakarta, Indonesia, APAC"),

    NORTHAMERICA_NORTHEAST1("northamerica-northeast1", "Montreal, Quebec, North America"),

    SOUTHAMERICA_EAST1("southamerica-east1", "Osasco (São Paulo), Brazil, South America"),

    AUSTRALIA_SOUTHEAST1("australia-southeast1", "Sydney, Australia, Australia")

    ;


    private final String regionCode;
    private final String regionName;

    RegionGCP(String regionCode, String regionName) {
        this.regionCode = regionCode;
        this.regionName = regionName;
    }


    public static RegionGCP fromCode(String code) {
        for (RegionGCP region : RegionGCP.values()) {
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

        // Split regionName by comma and set correct continent, country, and city

        String[] regionNameParts = regionName.split(", ");

        if(regionNameParts.length != 3) {
            throw new HolisticFaaSException("Region name must consist of continent, country, and city. Region name: " + regionName);
        }

        RegionGlobal regionGlobal = new RegionGlobal();

        regionGlobal.setContinent(regionNameParts[2]);
        regionGlobal.setCountry(regionNameParts[1]);
        regionGlobal.setCity(regionNameParts[0]);

        return regionGlobal;

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