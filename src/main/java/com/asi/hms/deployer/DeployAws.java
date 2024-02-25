package com.asi.hms.deployer;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.Function;
import com.asi.hms.model.Role;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.*;
import software.amazon.awssdk.services.lambda.waiters.LambdaWaiter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

// TODO: use a logger
public class DeployAws implements DeployInterface {

    @Override
    public boolean deployFunction(Function function, Role role) throws HolisticFaaSException {

        LambdaClient awsLambda = LambdaClient.builder()
                .region(function.getRegion().toAwsRegion())
                .build();

        try {

            LambdaWaiter waiter = awsLambda.waiter();

            // TODO: What file paths are allowed and possible?
            InputStream is = new FileInputStream(function.getSource());
            SdkBytes fileToUpload = SdkBytes.fromInputStream(is);

            FunctionCode code = FunctionCode.builder()
                    .zipFile(fileToUpload)
                    .build();

            CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
                    .functionName(function.getName())
                    .handler(function.getHandler())
                    .role(role.getArn()) // Get the ARN of the role
                    .runtime(function.getRuntime())
                    .code(code)
                    .memorySize(function.getMemory())
                    .timeout(function.getTimeout())
                    .build();

            // Create a Lambda function using a waiter
            CreateFunctionResponse functionResponse = awsLambda.createFunction(functionRequest);
            GetFunctionRequest getFunctionRequest = GetFunctionRequest.builder()
                    .functionName(function.getName())
                    .build();

            WaiterResponse<GetFunctionResponse> waiterResponse = waiter.waitUntilFunctionExists(getFunctionRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println("Function created with arn: " + functionResponse.functionArn());

        } catch (LambdaException | FileNotFoundException e) {

            throw new HolisticFaaSException(e.getMessage());

        }

        awsLambda.close();

        return true;

    }

}
