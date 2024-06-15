package com.asi.hms.service;

import com.asi.hms.model.api.APISystemInfo;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

@Service
public class SystemService {

    private final BuildProperties buildProperties;

    public SystemService(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public APISystemInfo getSystemInfo() {

        APISystemInfo systemInfo = new APISystemInfo();
        systemInfo.setSystemName(buildProperties.getName());
        systemInfo.setSystemVersion(buildProperties.getVersion());
        systemInfo.setTime(buildProperties.getTime());

        return systemInfo;

    }

}
