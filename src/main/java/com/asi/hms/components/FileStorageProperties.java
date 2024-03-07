package com.asi.hms.components;

import com.asi.hms.controller.FunctionController;
import com.asi.hms.exceptions.HolisticFaaSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileStorageProperties {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageProperties.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {

        this.rootLocation = Paths.get(uploadDir);

        try {

            Path directories = Files.createDirectories(this.rootLocation);

            String absolutePath = directories.toAbsolutePath().toString();

            logger.info("Upload directory created: {}", absolutePath);

        } catch (Exception e) {

            throw new HolisticFaaSException("Could not create upload directory!");

        }

    }

    public String getUploadDir() {
        return uploadDir;
    }

}
