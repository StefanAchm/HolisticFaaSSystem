package com.asi.hms.model.api;

import com.asi.hms.model.db.DBFunction;

public class APIFunction2 {

    private String name;

    private APIFunctionType functionType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public APIFunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(APIFunctionType functionType) {
        this.functionType = functionType;
    }

    public static APIFunction2 fromDBFunction(DBFunction dbFunction) {
        APIFunction2 apiFunction = new APIFunction2();
        apiFunction.setName(dbFunction.getName());
        apiFunction.setFunctionType(APIFunctionType.fromDBFunctionType(dbFunction.getFunctionType()));
        return apiFunction;
    }
}
