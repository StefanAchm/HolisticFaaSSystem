package com.asi.hms.utils.cloudproviderutils;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.Message;
import com.asi.hms.model.UserAWS;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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

    private static final Logger logger = LoggerFactory.getLogger(DeployAWS.class);

    private static final int STEPS = 7;

    @Override
    public boolean deployFunction(Function function, UserAWS user, MessageInterface messageInterface) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(user.getAccessKeyId(), user.getSecretAccessKey());

        messageInterface.sendMessage(new Message(function.getId(), 1, STEPS, "Created AWS credentials"));

        LambdaClient awsLambda = LambdaClient.builder()
                .region(Region.of(function.getRegion().getRegionName()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        messageInterface.sendMessage(new Message(function.getId(), 2, STEPS, "Created AWS Lambda client"));

        try {

            LambdaWaiter waiter = awsLambda.waiter();

            InputStream is = Files.newInputStream(function.getFilePath());
            SdkBytes fileToUpload = SdkBytes.fromInputStream(is);

            FunctionCode code = FunctionCode.builder()
                    .zipFile(fileToUpload)
                    .build();

            messageInterface.sendMessage(new Message(function.getId(), 3, STEPS, "Created function code"));

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                    .functionName(function.getName())
                    .handler(function.getHandler())
                    .role(user.getRoleArn()) // The ARN of the role
                    .runtime(function.getRuntime().getRuntimeString())
                    .code(code)
                    .memorySize(function.getMemory())
                    .timeout(function.getTimeoutSecs()) // Timeout in seconds
                    .build();

            messageInterface.sendMessage(new Message(function.getId(), 4, STEPS, "Created function request, creating function now (this may take a while)"));

            // Create a Lambda function using a waiter
            CreateFunctionResponse functionResponse = awsLambda.createFunction(functionRequest);

            GetFunctionRequest getFunctionRequest = GetFunctionRequest.builder()
                    .functionName(function.getName())
                    .build();

            messageInterface.sendMessage(new Message(function.getId(), 5, STEPS, "Created function response"));

            WaiterResponse<GetFunctionResponse> waiterResponse = waiter.waitUntilFunctionExists(getFunctionRequest);
            waiterResponse
                    .matched()
                    .response()
                    .ifPresent(l -> messageInterface.sendMessage(new Message(function.getId(), 6, STEPS, "Function created with arn:" + functionResponse.functionArn())));

            messageInterface.sendMessage(new Message(function.getId(), 7, STEPS, "Function created"));

            awsLambda.close();

        } catch (LambdaException | IOException e) {

            messageInterface.sendMessage(new Message(function.getId(), STEPS, STEPS, "Failed to create function: " + e.getMessage()));

            throw new HolisticFaaSException(e);

        }

        return true;

    }

}
