package com.asi.hms.model.api;

import com.asi.hms.enums.Provider;

import java.util.List;

public class APIMigration {

    private Provider provider;
    private List<APIFunction> functions;

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<APIFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<APIFunction> functions) {
        this.functions = functions;
    }
}
