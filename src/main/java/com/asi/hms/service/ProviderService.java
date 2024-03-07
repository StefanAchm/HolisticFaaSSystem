package com.asi.hms.service;

import com.asi.hms.enums.*;
import com.asi.hms.model.api.APIProviderOptions;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProviderService {


    public List<APIProviderOptions> getProviderOptions() {

        APIProviderOptions awsOptions = new APIProviderOptions();
        awsOptions.setProvider(Provider.AWS);
        awsOptions.setRegions(Arrays.asList(RegionAWS.values()));
        awsOptions.setRuntimes(Arrays.asList(RuntimeAWS.values()));

        APIProviderOptions gcpOptions = new APIProviderOptions();
        gcpOptions.setProvider(Provider.GCP);
        gcpOptions.setRegions(Arrays.asList(RegionGCP.values()));
        gcpOptions.setRuntimes(Arrays.asList(RuntimeGCP.values()));

        return Arrays.asList(awsOptions, gcpOptions);

    }

}
