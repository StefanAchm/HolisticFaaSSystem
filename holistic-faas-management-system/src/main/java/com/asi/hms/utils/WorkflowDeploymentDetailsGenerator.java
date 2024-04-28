package com.asi.hms.utils;

import com.asi.hms.model.db.DBFunctionDeployment;
import com.asi.hms.model.db.DBWorkflowDeployment;

import java.util.List;

public class WorkflowDeploymentDetailsGenerator {

    public static String getDeploymentDetails(DBWorkflowDeployment deployment) {

        // Build a String, which contains common details of the deployment
        // First should be provider, then the region, then the runtime, then the memory, then the timeout
        // If all have the same provider, set the provider (AWS or GCP), else set the provider as "Multiple"

        if(deployment.getFunctionDeployments() == null || deployment.getFunctionDeployments().size() == 0) {
            return "";
        }

        return "" + getProvider(deployment.getFunctionDeployments()) +
                "_" + getRegion(deployment.getFunctionDeployments()) +
                "_" + getRuntime(deployment.getFunctionDeployments()) +
                "_" + getMemory(deployment.getFunctionDeployments()) + "mb" +
                "_" + getTimeout(deployment.getFunctionDeployments()) + "s";

    }

    private static String getProvider(List<DBFunctionDeployment> functionDeployments) {

        // Get the provider of the first function deployment
        String provider = functionDeployments.get(0).getProvider();

        // Check if all function deployments have the same provider
        for (DBFunctionDeployment functionDeployment : functionDeployments) {
            if (!functionDeployment.getProvider().equals(provider)) {
                return "XXX";
            }
        }

        return provider;

    }

    private static String getRegion(List<DBFunctionDeployment> functionDeployments) {

        // Get the region of the first function deployment
        String region = functionDeployments.get(0).getRegion();

        // Check if all function deployments have the same region
        for (DBFunctionDeployment functionDeployment : functionDeployments) {
            if (!functionDeployment.getRegion().equals(region)) {
                return "XXX";
            }
        }

        return region;

    }

    private static String getRuntime(List<DBFunctionDeployment> functionDeployments) {

        // Get the runtime of the first function deployment
        String runtime = functionDeployments.get(0).getRuntime();

        // Check if all function deployments have the same runtime
        for (DBFunctionDeployment functionDeployment : functionDeployments) {
            if (!functionDeployment.getRuntime().equals(runtime)) {
                return "XXX";
            }
        }

        return runtime;

    }

    private static String getMemory(List<DBFunctionDeployment> functionDeployments) {

        // Get the memory of the first function deployment
        Integer memory = functionDeployments.get(0).getMemory();

        // Check if all function deployments have the same memory
        for (DBFunctionDeployment functionDeployment : functionDeployments) {
            if (!functionDeployment.getMemory().equals(memory)) {
                return "XXX";
            }
        }

        return memory.toString();

    }

    private static String getTimeout(List<DBFunctionDeployment> functionDeployments) {

        // Get the timeout of the first function deployment
        Integer timeout = functionDeployments.get(0).getTimeoutSecs();

        // Check if all function deployments have the same timeout
        for (DBFunctionDeployment functionDeployment : functionDeployments) {
            if (!functionDeployment.getTimeoutSecs().equals(timeout)) {
                return "XXX";
            }
        }

        return timeout.toString();

    }

}
