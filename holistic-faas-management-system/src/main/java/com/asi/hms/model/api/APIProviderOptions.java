package com.asi.hms.model.api;

import com.asi.hms.enums.Provider;
import com.asi.hms.enums.RegionInterface;
import com.asi.hms.enums.RuntimeInterface;

import java.util.List;

public class APIProviderOptions {

    private Provider provider;
    private List<RegionInterface> regions;
    private List<RuntimeInterface> runtimes;

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<RegionInterface> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionInterface> regions) {
        this.regions = regions;
    }

    public List<RuntimeInterface> getRuntimes() {
        return runtimes;
    }

    public void setRuntimes(List<RuntimeInterface> runtimes) {
        this.runtimes = runtimes;
    }
}
