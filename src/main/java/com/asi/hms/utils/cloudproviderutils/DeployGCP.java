package com.asi.hms.utils.cloudproviderutils;


import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.UserGCP;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.google.cloud.functions.v1.*;
import com.google.cloud.storage.*;
import com.google.protobuf.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

public class DeployGCP implements DeployInterface<UserGCP> {

    private static final Logger logger = LoggerFactory.getLogger(DeployGCP.class);

    @Override
    public boolean deployFunction(Function function, UserGCP user) throws HolisticFaaSException {

        String bucketName = getBucketName(function);

        createBucket(user, bucketName);

        String sourceZipFile = uploadZipToBucket(function, user, bucketName); // or use the bucketNameExt

        return createFunction(function, user, sourceZipFile);

    }

    private static String getBucketName(Function function) {

        String name = function.getName().toLowerCase().replaceAll("[^a-z0-9]", "-");

        return name + "-bucket";

    }

    private static boolean createFunction(Function function, UserGCP user, String sourceZipFile) throws HolisticFaaSException {

        CloudFunctionsServiceSettings cloudFunctionsServiceSettings;

        try {

            cloudFunctionsServiceSettings = CloudFunctionsServiceSettings.newBuilder()
                    .setCredentialsProvider(user::getGoogleCredentials)
                    .build();

        } catch (IOException e) {

            throw new HolisticFaaSException(e.getMessage());

        }

        try (CloudFunctionsServiceClient client = CloudFunctionsServiceClient.create(cloudFunctionsServiceSettings)) {

            String parent = LocationName.format(user.getProjectName(), function.getRegionInterface().getRegionName());

            CloudFunction cloudFunction = CloudFunction.newBuilder()
                    .setName(parent + "/functions/" + function.getName())
                    .setEntryPoint(function.getHandler())
                    .setRuntime(function.getRuntimeInterface().getRuntimeString())
                    .setHttpsTrigger(HttpsTrigger.newBuilder().build()) // Not sure if https trigger is needed
                    .setSourceArchiveUrl(sourceZipFile)
                    .setAvailableMemoryMb(function.getMemory())
                    .setTimeout(Duration.newBuilder().setSeconds(function.getTimeoutSecs()).build())
                    .build();

            CreateFunctionRequest request = CreateFunctionRequest.newBuilder()
                    .setLocation(parent)
                    .setFunction(cloudFunction)
                    .build();

            CloudFunction cloudFunction1 = client.createFunctionAsync(request).get();

            logger.info("Function created: {}", cloudFunction1);


        } catch (IOException | ExecutionException e) {

            throw new HolisticFaaSException(e.getMessage());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // ????
        }

        return true;

    }

    private String createBucket(UserGCP user, String bucketName) {

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(user.getGoogleCredentials())
                .setProjectId(user.getProjectName())
                .build().getService();

        Bucket bucket = storage.create(BucketInfo.of(bucketName));

        logger.info("Bucket {} created.", bucket.getName());

        return "gs://" + bucketName;

    }


    // Assuming sourceZipFilePath is a path to a zip file in Google Cloud Storage (gs://bucket-name/path/to/function.zip)
    private String uploadZipToBucket(Function function, UserGCP user, String bucketName) throws HolisticFaaSException {

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(user.getGoogleCredentials())
                .setProjectId(user.getProjectName())
                .build().getService();


        String objectName = "function.zip";

        // Prepares the file to upload
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();


        try {

            byte[] bytes = Files.readAllBytes(function.getFilePath());
            storage.create(blobInfo, bytes);

            logger.info("File {} uploaded to bucket {} as {}", function.getFilePath(), bucketName, objectName);

            return "gs://" + bucketName + "/" + objectName;

        } catch (IOException e) {
            throw new HolisticFaaSException(e.getMessage());
        }

    }

}
