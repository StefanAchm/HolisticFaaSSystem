package com.asi.hms.service;

import com.asi.hms.config.properties.FileStorageProperties;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class UploadFileService {

    private static final Logger logger = LoggerFactory.getLogger(UploadFileService.class);

    public static final String FUNCTIONS_DIR = "functions";
    public static final String UPLOADS_DIR = "deployments";

    private final FileStorageProperties fileStorageProperties;

    public UploadFileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    /**
     * Uploads a zip file and unzips it
     *
     * @param file      The file to be uploaded
     * @param subFolder The subfolder where the file should be uploaded
     * @return The path where the file is uploaded and unzipped
     */
    public Path uploadZipFileAndUnzip(MultipartFile file, String subFolder) {

        Path uploadedFilePath = uploadFile(file, subFolder, true);

        return FileUtil.unzip(uploadedFilePath);

    }

    /**
     * Uploads a file and normalizes the path <br>
     *
     * @param file      The file to be uploaded
     * @param subFolder The subfolder where the file should be uploaded
     * @param forceZip  If true, it ensures that the file is zipped (either it is already a zip file or it is zipped by this method)
     * @return The normalized path as a string
     */
    public String uploadFileAndNormalize(MultipartFile file, String subFolder, boolean forceZip) {

        return uploadFile(file, subFolder, forceZip)
                .normalize()
                .toString();

    }


    /**
     * @param file      The file to be uploaded
     * @param subFolder The subfolder where the file should be uploaded
     * @param forceZip  If true, it ensures that the file is zipped (either it is already a zip file or it is zipped by this method)
     * @return The path where the file is uploaded
     */
    public Path uploadFile(MultipartFile file, String subFolder, boolean forceZip) {

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

        Path destinationFile = destinationFolder.resolve(fileName);

        if (Files.exists(destinationFile)) {
            throw new HolisticFaaSException("File " + destinationFile + " already exists");
        }

        try {

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            logger.info("File uploaded to: {}", destinationFile.toAbsolutePath());

            // If it is not a zip file, create a zip file
            if (forceZip && !fileName.endsWith(".zip")) {

                String fileNameWithoutEnding = fileName.substring(0, fileName.lastIndexOf("."));
                Path destinationFileAsZip = destinationFolder.resolve(fileNameWithoutEnding + ".zip");

                Files.createFile(destinationFileAsZip);
                FileUtil.zipFiles(List.of(destinationFile), destinationFileAsZip);

                destinationFile = destinationFileAsZip;

                logger.info("File zipped to: {}", destinationFile.toAbsolutePath());

            }

            return destinationFile.toAbsolutePath();

        } catch (Exception e) {

            throw new HolisticFaaSException("Error uploading file, " + e.getMessage());

        }

    }

}
