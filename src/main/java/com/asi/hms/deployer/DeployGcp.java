package com.asi.hms.deployer;


import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.Role;
import com.asi.hms.model.User;
import com.google.cloud.functions.v1.*;
import com.google.cloud.storage.*;
import com.google.protobuf.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

public class DeployGcp implements DeployInterface {

    @Override
    public boolean deployFunction(Function function, Role role, User user) throws HolisticFaaSException {

        String bucketName = getBucketName(function);

        String bucketNameExt = createBucket(function, user, bucketName);

        String sourceZipFile = uploadZipToBucket(function, user, bucketName); // or use the bucketNameExt

        return createFunction(function, user, sourceZipFile);

    }

    private static String getBucketName(Function function) {

        String name = function.getName().toLowerCase().replaceAll("[^a-z0-9]", "-");

        return name + "-bucket";

    }

    private static boolean createFunction(Function function, User user, String sourceZipFile) throws HolisticFaaSException {

        CloudFunctionsServiceSettings cloudFunctionsServiceSettings;

        try {

            cloudFunctionsServiceSettings = CloudFunctionsServiceSettings.newBuilder()
                    .setCredentialsProvider(user::getGoogleCredentials)
                    .build();

        } catch (IOException e) {

            throw new HolisticFaaSException(e.getMessage());

        }

        try (CloudFunctionsServiceClient client = CloudFunctionsServiceClient.create(cloudFunctionsServiceSettings)) {

            String parent = LocationName.format(function.getProjectName(), function.getRegion().toGcpRegion());

            CloudFunction cloudFunction = CloudFunction.newBuilder()
                    .setName(parent + "/functions/" + function.getName())
                    .setEntryPoint(function.getHandler())
                    .setRuntime(function.getRuntime())
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
            System.out.println("Function created: " + cloudFunction1);


        } catch (IOException | ExecutionException e) {

            e.printStackTrace();

            throw new HolisticFaaSException(e.getMessage());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // ????
        }

        return true;

    }

    private String createBucket(Function function, User user, String bucketName) {

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(user.getGoogleCredentials())
                .setProjectId(function.getProjectName())
                .build().getService();

        Bucket bucket = storage.create(BucketInfo.of(bucketName));

        System.out.println("Bucket " + bucket.getName() + " created.");

        return "gs://" + bucketName;

    }


    // Assuming sourceZipFilePath is a path to a zip file in Google Cloud Storage (gs://bucket-name/path/to/function.zip)
    private String uploadZipToBucket(Function function, User user, String bucketName) throws HolisticFaaSException {

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(user.getGoogleCredentials())
                .setProjectId(function.getProjectName())
                .build().getService();


        String objectName = "function.zip";

        // Prepares the file to upload
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();


        try {

            byte[] bytes = Files.readAllBytes(function.getFilePath());
            Blob blob = storage.create(blobInfo, bytes);

            System.out.println("File " + function.getFilePath().toString() + " uploaded to bucket " + bucketName + " as " + objectName);
            System.out.println("Blob: " + blob);

            return "gs://" + bucketName + "/" + objectName;

        } catch (IOException e) {
            throw new HolisticFaaSException(e.getMessage());
        }

    }

}
