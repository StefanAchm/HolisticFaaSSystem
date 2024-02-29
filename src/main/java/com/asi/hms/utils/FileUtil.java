package com.asi.hms.utils;

import com.asi.hms.exceptions.HolisticFaaSException;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns the absolute path of a file in the resources folder (as URL)
     */
    public static Path getFilePathFromResourcesFile(String fileName) throws HolisticFaaSException {

        URL resource = FileUtil.class.getClassLoader().getResource(fileName);

        if (resource == null) {
            throw new HolisticFaaSException("File not found: " + fileName);
        } else {

            try {

                return Paths.get(resource.toURI());

            } catch (URISyntaxException e) {

                throw new HolisticFaaSException("Error reading file: " + fileName);

            }

        }

    }

//    public static String getFilePathFromResourcesFileAsURI(String fileName) throws HolisticFaaSException {
//
//        URL resource = FileUtil.class.getClassLoader().getResource(fileName);
//
//        if (resource == null) {
//            throw new HolisticFaaSException("File not found: " + fileName);
//        } else {
//            return resource.toURI();
//        }
//
//    }

    public static Properties getPropertiesFromResourcesFile(String fileName) throws HolisticFaaSException {

        Properties properties = new Properties();

        try {

            properties.load(FileUtil.class.getClassLoader().getResourceAsStream(fileName));

        } catch (Exception e) {
            throw new HolisticFaaSException("Error reading properties file: " + fileName);
        }

        return properties;

    }

    public static InputStream readFile(String fileName) throws HolisticFaaSException {
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new HolisticFaaSException("File not found: " + fileName);
        } else {
            return inputStream;
        }
    }

}
