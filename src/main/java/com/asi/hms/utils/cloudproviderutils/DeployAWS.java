package com.asi.hms.utils.cloudproviderutils;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
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

    @Override
    public boolean deployFunction(Function function, UserAWS user) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(user.getAccessKeyId(), user.getSecretAccessKey());

        logger.info("Created AWS credentials");

        LambdaClient awsLambda = LambdaClient.builder()
                .region(Region.of(function.getRegion().getRegionName())) // TODO: validate?
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        logger.info("Created AWS Lambda client");

        try {

            LambdaWaiter waiter = awsLambda.waiter();

            // TODO: What file paths are allowed and possible?
            InputStream is = Files.newInputStream(function.getFilePath());
            SdkBytes fileToUpload = SdkBytes.fromInputStream(is);

            FunctionCode code = FunctionCode.builder()
                    .zipFile(fileToUpload)
                    .build();

            logger.info("Created function code");

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                    .functionName(function.getName())
                    .handler(function.getHandler())
                    .role(user.getRoleArn()) // The ARN of the role
                    .runtime(function.getRuntime().getRuntimeString())
                    .code(code)
                    .memorySize(function.getMemory())
                    .timeout(function.getTimeoutSecs()) // Timeout in seconds
                    .build();

            logger.info("Created function request");

            logger.info("Creating function (this may take a while)");

            // Create a Lambda function using a waiter
            CreateFunctionResponse functionResponse = awsLambda.createFunction(functionRequest);

            GetFunctionRequest getFunctionRequest = GetFunctionRequest.builder()
                    .functionName(function.getName())
                    .build();

            logger.info("Created function response");

            WaiterResponse<GetFunctionResponse> waiterResponse = waiter.waitUntilFunctionExists(getFunctionRequest);
            waiterResponse.matched().response()
                    .ifPresent(l -> logger.info("Function created with arn: {}", functionResponse.functionArn()));

        } catch (LambdaException | IOException e) {

            throw new HolisticFaaSException(e.getMessage());

        }

        logger.info("Closing AWS Lambda client");

        awsLambda.close();

        return true;

    }

}
