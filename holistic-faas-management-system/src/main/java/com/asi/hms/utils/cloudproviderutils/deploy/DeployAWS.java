package com.asi.hms.utils.cloudproviderutils.deploy;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.utils.cloudproviderutils.model.UserAWS;
import com.asi.hms.utils.ProgressHandler;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.*;
import software.amazon.awssdk.services.lambda.waiters.LambdaWaiter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class DeployAWS implements DeployInterface<UserAWS> {

    public static final int STEPS = 6;

    @Override
    public boolean deployFunction(Function function, UserAWS user, ProgressHandler progressHandler) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(user.getAccessKeyId(), user.getSecretAccessKey());

        progressHandler.update("Created AWS credentials");

        LambdaClient awsLambda = LambdaClient.builder()
                .region(Region.of(function.getRegion().getRegionCode()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        progressHandler.update("Created AWS Lambda client");

        try {

            LambdaWaiter waiter = awsLambda.waiter();

            InputStream is = Files.newInputStream(function.getFilePath());
            SdkBytes fileToUpload = SdkBytes.fromInputStream(is);

            FunctionCode code = FunctionCode.builder()
                    .zipFile(fileToUpload)
                    .build();

            progressHandler.update("Created function code");

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                    .functionName(function.getName())
                    .handler(function.getHandler())
                    .role(user.getRoleArn()) // The ARN of the role
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

            WaiterResponse<GetFunctionResponse> waiterResponse = waiter.waitUntilFunctionExists(getFunctionRequest);
            waiterResponse
                    .matched()
                    .response()
                    .ifPresent(l -> progressHandler.update("Function created with arn:" + functionResponse.functionArn()));

            awsLambda.close();

        } catch (LambdaException | IOException e) {

            throw new HolisticFaaSException(e);

        }

        return true;

    }

}
