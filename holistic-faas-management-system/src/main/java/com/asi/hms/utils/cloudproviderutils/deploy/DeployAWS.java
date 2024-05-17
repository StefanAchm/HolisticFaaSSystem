package com.asi.hms.utils.cloudproviderutils.deploy;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.cloudproviderutils.model.Function;
import com.asi.hms.model.UserAWS;
import com.asi.hms.utils.ProgressHandler;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.IamClientBuilder;
import software.amazon.awssdk.services.iam.model.*;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.*;
import software.amazon.awssdk.services.lambda.waiters.LambdaWaiter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class DeployAWS implements DeployerInterface {

    private static final String ROLE_NAME = "lambda-execution-role-hf";

    public static final int STEPS = 6;

    private final UserAWS user;

    public DeployAWS(UserAWS user) {
        this.user = user;
    }

    @Override
    public String deployFunction(Function function, ProgressHandler progressHandler) {

        LambdaClient awsLambda = createLambdaClient(function, progressHandler);

        String roleArn = getOrCreateRoleArn();

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
                    .role(roleArn)
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

            return functionResponse.functionArn();

        } catch (LambdaException | IOException e) {
            throw new HolisticFaaSException(e);
        }

    }

    private String getOrCreateRoleArn() {

        if(this.user.getRoleArn() != null) {

            return this.user.getRoleArn();

        } else {

            // Check if role already exists and return its ARN
            String roleArn = getExistingRoleArn();
            if(roleArn != null) {
                return roleArn;
            }

            return getAndCreateNewRoleArn();

        }

    }

    private String getAndCreateNewRoleArn() {

        CreateRoleResponse createRoleResponse;

        IamClientBuilder iamClientBuilder = IamClient.builder();
        iamClientBuilder.region(Region.AWS_GLOBAL);
        iamClientBuilder.credentialsProvider(StaticCredentialsProvider.create(user.getAwsCredentials()));

        try (IamClient iamClient = iamClientBuilder.build()) {

            // Create the IAM role
            String assumeRolePolicyDocument = "{"
                    + "\"Version\": \"2012-10-17\","
                    + "\"Statement\": ["
                    + "{"
                    + "\"Effect\": \"Allow\","
                    + "\"Principal\": {"
                    + "\"Service\": \"lambda.amazonaws.com\""
                    + "},"
                    + "\"Action\": \"sts:AssumeRole\""
                    + "}"
                    + "]"
                    + "}";

            CreateRoleRequest createRoleRequest = CreateRoleRequest.builder()
                    .roleName(ROLE_NAME)
                    .assumeRolePolicyDocument(assumeRolePolicyDocument)
                    .description("Role for Lambda execution")
                    .build();

            createRoleResponse = iamClient.createRole(createRoleRequest);
        }

        return createRoleResponse.role().arn();

    }

    private String getExistingRoleArn() {

        IamClientBuilder iamClientBuilder = IamClient.builder();
        iamClientBuilder.region(Region.AWS_GLOBAL);
        iamClientBuilder.credentialsProvider(StaticCredentialsProvider.create(user.getAwsCredentials()));

        try (IamClient iamClient = iamClientBuilder.build()) {

            ListRolesRequest listRolesRequest = ListRolesRequest.builder().build();
            ListRolesResponse listRolesResponse = iamClient.listRoles(listRolesRequest);

            return listRolesResponse.roles().stream()
                    .filter(r -> r.roleName().equals(ROLE_NAME))
                    .findFirst()
                    .map(Role::arn)
                    .orElse(null);

        }

    }

    private LambdaClient createLambdaClient(Function function, ProgressHandler progressHandler) {

        progressHandler.update("Created AWS Lambda client ...");

        LambdaClient awsLambda = LambdaClient.builder()
                .region(Region.of(function.getRegion().getRegionCode()))
                .credentialsProvider(StaticCredentialsProvider.create(user.getAwsCredentials()))
                .build();

        progressHandler.update("Created AWS Lambda client");

        return awsLambda;

    }

    @Override
    public void updateFunction(Function function, ProgressHandler progressHandler) {

        try {

            LambdaClient awsLambda = createLambdaClient(function, progressHandler);

            String roleArn = getOrCreateRoleArn();

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
                    .role(roleArn)
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

            awsLambda.close();

        } catch (LambdaException | IOException e) {
            throw new HolisticFaaSException(e);
        }

    }

}
