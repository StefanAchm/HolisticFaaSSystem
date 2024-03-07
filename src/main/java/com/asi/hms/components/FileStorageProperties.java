package com.asi.hms.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileStorageProperties {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

}
