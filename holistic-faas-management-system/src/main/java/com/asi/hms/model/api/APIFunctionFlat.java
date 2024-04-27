package com.asi.hms.model.api;

import com.asi.hms.model.db.DBFunctionDeployment;

public class APIFunctionFlat {

    private APIFunction function;
    private APIFunctionType functionType;
    private APIFunctionImplementation functionImplementation;
    private APIFunctionDeployment functionDeployment;

    public static APIFunctionFlat fromDBFunction(DBFunctionDeployment dbFunctionDeployment) {
        APIFunctionFlat apiFunctionFlat = new APIFunctionFlat();
        apiFunctionFlat.setFunction(APIFunction.fromDBFunction(dbFunctionDeployment.getFunction()));
        apiFunctionFlat.setFunctionType(APIFunctionType.fromDBFunctionType(dbFunctionDeployment.getFunction().getFunctionType()));
        apiFunctionFlat.setFunctionImplementation(APIFunctionImplementation.fromDBFunctionImplementation(dbFunctionDeployment.getFunctionImplementation()));
        apiFunctionFlat.setFunctionDeployment(APIFunctionDeployment.fromDBFunctionDeployment(dbFunctionDeployment));
        return apiFunctionFlat;
    }

    public APIFunction getFunction() {
        return function;
    }

    public void setFunction(APIFunction function) {
        this.function = function;
    }

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
