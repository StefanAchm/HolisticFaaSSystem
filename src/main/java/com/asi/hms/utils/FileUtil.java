package com.asi.hms.utils;

import com.asi.hms.exceptions.HolisticFaaSException;

import java.net.URL;
import java.util.Properties;

public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getFilePathFromResourcesFile(String fileName) throws HolisticFaaSException {

        URL resource = FileUtil.class.getClassLoader().getResource(fileName);

        if (resource == null) {
            throw new HolisticFaaSException("File not found: " + fileName);
        } else {
            return resource.getFile();
        }

    }

    public static Properties getPropertiesFromResourcesFile(String fileName) throws HolisticFaaSException {

        Properties properties = new Properties();

        try {

            properties.load(FileUtil.class.getClassLoader().getResourceAsStream(fileName));

        } catch (Exception e) {
            throw new HolisticFaaSException("Error reading properties file: " + fileName);
        }

        return properties;

    }

}
