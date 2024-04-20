package com.asi.hms.utils;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public static String getFileNameFromPath(String filePath) {

        if(filePath == null) {
            return null;
        }

        return filePath.substring(filePath.lastIndexOf("\\") + 1);

    }

    public static String normalize(Path filePath) {
        return filePath.normalize().toString();
    }

    public static Path unzip(Path filePath) {

        String newFilePath = filePath.toString().replace(".zip", "");
        Path path = Paths.get(newFilePath);

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new HolisticFaaSException("Could not create directory " + path);
        }

        // TODO: This does only work, if the file is a zip file and has no subdirectories!!!

        // Unpack the zip file
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(filePath.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(path.toFile(), zipEntry);
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            throw new HolisticFaaSException(e);
        }

        return path;

    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {

        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;

    }

    public static File getFileWithExtension(File rootPath, List<String> yaml) {

        // Get all files in the directory
        File[] files = rootPath.listFiles();

        if (files == null) {
            throw new HolisticFaaSException("No files found in directory: " + rootPath.getAbsolutePath());
        }

        for (File file : files) {
            if (file.isFile()) {
                for (String extension : yaml) {
                    if (file.getName().endsWith(extension)) {
                        return file;
                    }
                }
            }
        }

        throw new HolisticFaaSException("No files with extension " + yaml + " found in directory: " + rootPath.getAbsolutePath());

    }

}
