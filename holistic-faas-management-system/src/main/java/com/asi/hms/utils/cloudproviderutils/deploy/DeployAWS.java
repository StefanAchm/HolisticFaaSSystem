package com.asi.hms.utils.cloudproviderutils.deploy;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.model.UserAWS;
import com.asi.hms.utils.ProgressHandler;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.*;
import software.amazon.awssdk.services.lambda.waiters.LambdaWaiter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class DeployAWS implements DeployerInterface {

    public static final int STEPS = 6;

    private final UserAWS user;

    public DeployAWS(UserAWS user) {
        this.user = user;
    }

    @Override
    public boolean deployFunction(Function function, ProgressHandler progressHandler) {

        LambdaClient awsLambda = createLambdaClient(function, progressHandler);

        try {

            InputStream is = Files.newInputStream(function.getFilePath());
            SdkBytes fileToUpload = SdkBytes.fromInputStream(is);

            FunctionCode code = FunctionCode.builder()
                    .zipFile(fileToUpload)
                    .build();

            progressHandler.update("Created function code");

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                    .functionName(function.getName())
                    .handler(function.getHandler())
                    .role(this.user.getRoleArn()) // The ARN of the role
                    .runtime(function.getRuntime().getRuntimeCode())
                    .code(code)
                    .memorySize(function.getMemory())
                    .timeout(function.getTimeoutSecs()) // Timeout in seconds
                    .build();

            progressHandler.update("Created function request, creating function now (this may take a while)");

            // Create a Lambda function using a waiter
            CreateFunctionResponse functionResponse = awsLambda.createFunction(functionRequest);

            GetFunctionRequest getFunctionRequest = GetFunctionRequest.builder()
                    .functionName(function.getName())
                    .build();

            progressHandler.update("Function created, waiting for function to exist");

            LambdaWaiter waiter = awsLambda.waiter();
            waiter.waitUntilFunctionExists(getFunctionRequest)
                    .matched()
                    .response()
                    .ifPresent(l -> progressHandler.update("Function created with arn:" + functionResponse.functionArn()));

            awsLambda.close();

        } catch (LambdaException | IOException e) {
            throw new HolisticFaaSException(e);
        }

        return true;

    }

    private LambdaClient createLambdaClient(Function function, ProgressHandler progressHandler) {

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                this.user.getAccessKeyId(),
                this.user.getSecretAccessKey()
        );

        progressHandler.update("Created AWS credentials");

        LambdaClient awsLambda = LambdaClient.builder()
                .region(Region.of(function.getRegion().getRegionCode()))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();

        progressHandler.update("Created AWS Lambda client");

        return awsLambda;

    }

    @Override
    public boolean updateFunction(Function function, ProgressHandler progressHandler) {

        try {

            LambdaClient awsLambda = createLambdaClient(function, progressHandler);

            LambdaWaiter waiter = awsLambda.waiter();

            // Update function code
            InputStream is = Files.newInputStream(function.getFilePath());
            SdkBytes fileToUpload = SdkBytes.fromInputStream(is);
            UpdateFunctionCodeRequest updateCodeRequest = UpdateFunctionCodeRequest.builder()
                    .functionName(function.getName())
                    .zipFile(fileToUpload)
                    .build();

            awsLambda.updateFunctionCode(updateCodeRequest);
            progressHandler.update("Updated function code");

            GetFunctionRequest getFunctionRequest = GetFunctionRequest.builder()
                    .functionName(function.getName())
                    .build();

            waiter.waitUntilFunctionUpdatedV2(getFunctionRequest)
                    .matched()
                    .response()
                    .ifPresent(l -> progressHandler.update("Function code updated"));


            // Update function configuration
            UpdateFunctionConfigurationRequest updateConfigRequest = UpdateFunctionConfigurationRequest.builder()
                    .functionName(function.getName())
                    .handler(function.getHandler())
                    .role(this.user.getRoleArn())
                    .runtime(function.getRuntime().getRuntimeCode())
                    .memorySize(function.getMemory())
                    .timeout(function.getTimeoutSecs())
                    .build();

            awsLambda.updateFunctionConfiguration(updateConfigRequest);
            progressHandler.update("Updated function configuration");

            waiter.waitUntilFunctionUpdatedV2(getFunctionRequest)
                    .matched()
                    .response()
                    .ifPresent(l -> progressHandler.update("Function configuration updated"));

            // Publish a new version
//            PublishVersionRequest publishVersionRequest = PublishVersionRequest.builder()
//                    .functionName(function.getName())
//                    .build();
//
//            awsLambda.publishVersion(publishVersionRequest);
//
//            waiter.waitUntilFunctionUpdatedV2(getFunctionRequest)
//                    .matched()
//                    .response()
//                    .ifPresent(l -> progressHandler.update("Function version published"));

            awsLambda.close();

        } catch (LambdaException | IOException e) {
            throw new HolisticFaaSException(e);
        }

        return true;

    }

}
