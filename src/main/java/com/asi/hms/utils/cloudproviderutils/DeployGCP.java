package com.asi.hms.utils.cloudproviderutils;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.UserGCP;
import com.asi.hms.utils.ProgressHandler;
import com.google.cloud.functions.v1.*;
import com.google.cloud.storage.*;
import com.google.protobuf.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

public class DeployGCP implements DeployInterface<UserGCP> {

    public static final int STEPS = 6;

    @Override
    public boolean deployFunction(Function function, UserGCP user, ProgressHandler progressHandler) {

        try {

            String bucketName = getBucketName(function);

            createBucket(user, bucketName, progressHandler);

            String sourceZipFile = uploadZipToBucket(function, user, bucketName, progressHandler); // or use the bucketNameExt

            return createFunction(function, user, sourceZipFile, progressHandler);

        } catch (Exception e) {

            // TODO: reinterruped exception

            throw new HolisticFaaSException(e);

        }

    }

    private static String getBucketName(Function function) {

        String name = function.getName().toLowerCase().replaceAll("[^a-z0-9]", "-");

        return name + "-bucket";

    }

    private static boolean createFunction(Function function, UserGCP user, String sourceZipFile, ProgressHandler progressHandler) throws IOException, ExecutionException, InterruptedException {

        CloudFunctionsServiceSettings cloudFunctionsServiceSettings;


        cloudFunctionsServiceSettings = CloudFunctionsServiceSettings.newBuilder()
                .setCredentialsProvider(user::getGoogleCredentials)
                .build();

        progressHandler.update("Created CloudFunctionsServiceSettings with credentials");

        try (CloudFunctionsServiceClient client = CloudFunctionsServiceClient.create(cloudFunctionsServiceSettings)) {

            String parent = LocationName.format(user.getProjectName(), function.getRegion().getRegionName());

            CloudFunction cloudFunction = CloudFunction.newBuilder()
                    .setName(parent + "/functions/" + function.getName())
                    .setEntryPoint(function.getHandler())
                    .setRuntime(function.getRuntime().getRuntimeString())
                    .setHttpsTrigger(HttpsTrigger.newBuilder().build()) // Not sure if https trigger is needed
                    .setSourceArchiveUrl(sourceZipFile)
                    .setAvailableMemoryMb(function.getMemory())
                    .setTimeout(Duration.newBuilder().setSeconds(function.getTimeoutSecs()).build())
                    .build();

            progressHandler.update("Created CloudFunction object");

            CreateFunctionRequest request = CreateFunctionRequest.newBuilder()
                    .setLocation(parent)
                    .setFunction(cloudFunction)
                    .build();


            progressHandler.update("Creating function (this may take a while)");

            CloudFunction cloudFunction1 = client.createFunctionAsync(request).get();

            progressHandler.update("Function created: " + cloudFunction1.getName());

        }

        return true;

    }

    private String createBucket(UserGCP user, String bucketName, ProgressHandler progressHandler) {

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(user.getGoogleCredentials())
                .setProjectId(user.getProjectName())
                .build().getService();

        Bucket bucket = storage.create(BucketInfo.of(bucketName));

        progressHandler.update("Bucket " + bucket.getName() + " created");

        return "gs://" + bucketName;

    }


    // Assuming sourceZipFilePath is a path to a zip file in Google Cloud Storage (gs://bucket-name/path/to/function.zip)
    private String uploadZipToBucket(Function function, UserGCP user, String bucketName, ProgressHandler progressHandler) throws IOException {

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(user.getGoogleCredentials())
                .setProjectId(user.getProjectName())
                .build().getService();


        String objectName = "function.zip";

        // Prepares the file to upload
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        byte[] bytes = Files.readAllBytes(function.getFilePath());
        storage.create(blobInfo, bytes);

        progressHandler.update("File " + function.getFilePath() + " uploaded to bucket " + bucketName + " as " + objectName);

        return "gs://" + bucketName + "/" + objectName;


    }

}
