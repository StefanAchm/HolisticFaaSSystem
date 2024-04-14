package com.asi.hms.service;

import com.asi.hms.config.properties.FileStorageProperties;
import com.asi.hms.exceptions.HolisticFaaSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UploadFileService {

    private static final Logger logger = LoggerFactory.getLogger(UploadFileService.class);

    public static final String FUNCTIONS_DIR = "functions";
    public static final String CREDENTIALS_DIR = "credentials";

    private final FileStorageProperties fileStorageProperties;

    public UploadFileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    public String uploadFileAndNormalize(MultipartFile file, String subFolder) {

        return uploadFile(file, subFolder).normalize().toString();

    }

    public Path uploadFile(MultipartFile file, String subFolder) {

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new HolisticFaaSException("File name is null");
        }

        // Create new folder

        Path destinationFolder = Paths
                .get(fileStorageProperties.getUploadDir())
                .resolve(subFolder)
                .resolve(UUID.randomUUID().toString());

        try {

            Files.createDirectories(destinationFolder);

            logger.info("Upload directory created: {}", destinationFolder.toAbsolutePath());

        } catch (IOException e) {
            throw new HolisticFaaSException("Could not create directory " + destinationFolder);
        }

        Path destinationPath = Paths.get(destinationFolder.toString()).resolve(fileName);

        if (Files.exists(destinationPath)) {
            throw new HolisticFaaSException("File " + destinationPath + " already exists");
        }

        try {

            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            logger.info("File uploaded to: {}", destinationPath.toAbsolutePath());

            return destinationPath.toAbsolutePath();

        } catch (Exception e) {

            throw new HolisticFaaSException("Error uploading file, " + e.getMessage());

        }

    }

}
