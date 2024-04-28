package com.asi.hms.utils;

import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.model.api.APIFunctionFlat;
import com.asi.hms.model.api.APIWorkflowDeployment;
import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBWorkflowDeployment;

import java.util.List;
import java.util.function.Function;

public class WorkflowDeploymentDetailsGenerator {

    private WorkflowDeploymentDetailsGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static String getDeploymentDetails(APIWorkflowDeployment deployment) {

        // Build a String, which contains common details of the deployment
        // First should be provider, then the region, then the runtime, then the memory, then the timeout
        // If all have the same provider, set the provider (AWS or GCP), else set the provider as "Multiple"

        if (deployment.getFunctionDefinitions() == null || deployment.getFunctionDefinitions().isEmpty()) {
            return "";
        }

        return          getCommonProperty(deployment.getFunctionDefinitions(), APIFunctionDeployment::getProvider) +
                "_" +   getCommonProperty(deployment.getFunctionDefinitions(), APIFunctionDeployment::getRegion) +
                "_" +   getCommonProperty(deployment.getFunctionDefinitions(), APIFunctionDeployment::getRuntime) +
                "_" +   getCommonProperty(deployment.getFunctionDefinitions(), APIFunctionDeployment::getMemory) + "mb" +
                "_" +   getCommonProperty(deployment.getFunctionDefinitions(), APIFunctionDeployment::getTimeoutSecs) + "s";
    }

    private static <T> String getCommonProperty(List<APIFunctionFlat> functionDeployments,
                                                Function<APIFunctionDeployment, T> propertyExtractor) {

        // Get the property of the first function deployment
        T property = propertyExtractor.apply(functionDeployments.get(0).getFunctionDeployment());

        if(property == null) {
            return "XXX";
        }

        // Check if all function deployments have the same property
        for (APIFunctionFlat functionDeployment : functionDeployments) {
            if (!property.equals(propertyExtractor.apply(functionDeployment.getFunctionDeployment()))) {
                return "XXX";
            }
        }

        return property.toString();

    }

}
