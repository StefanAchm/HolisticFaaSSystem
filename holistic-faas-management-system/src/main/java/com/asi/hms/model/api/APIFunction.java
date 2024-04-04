package com.asi.hms.model.api;

public class APIFunction {

    private APIFunctionType functionType;
    private APIFunctionImplementation functionImplementation;
    private APIFunctionDeployment functionDeployment;

    public APIFunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(APIFunctionType functionType) {
        this.functionType = functionType;
    }

    public APIFunctionImplementation getFunctionImplementation() {
        return functionImplementation;
    }

    public void setFunctionImplementation(APIFunctionImplementation functionImplementation) {
        this.functionImplementation = functionImplementation;
    }

    public APIFunctionDeployment getFunctionDeployment() {
        return functionDeployment;
    }

    public void setFunctionDeployment(APIFunctionDeployment functionDeployment) {
        this.functionDeployment = functionDeployment;
    }
}
