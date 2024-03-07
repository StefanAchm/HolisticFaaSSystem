package com.asi.hms.utils;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
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
    public static Path getFilePathFromResourcesFile(String fileName) {

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

    public static JsonObject getJsonFromFile(Path fileName) {

        try (InputStream inputStream = Paths.get(fileName.toString()).toUri().toURL().openStream()) {

            if (inputStream == null) {
                throw new HolisticFaaSException("File not found: " + fileName);
            } else {
                return JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            }

        } catch (Exception e) {

            throw new HolisticFaaSException("Error reading file: " + fileName);

        }


    }

    public static Properties getPropertiesFromFile(Path filePath) {

        Properties properties = new Properties();

        try (InputStream inputStream = Paths.get(filePath.toString()).toUri().toURL().openStream()) {

            properties.load(inputStream);

        } catch (Exception e) {

            throw new HolisticFaaSException("Error reading properties file: " + filePath);

        }

        return properties;

    }

}
