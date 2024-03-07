package com.asi.hms.model;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.FileUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserGCP implements UserInterface {

    private String projectName;
    private GoogleCredentials googleCredentials;

    public static UserGCP fromFile(Path filePath) {

        UserGCP user = new UserGCP();

        try (InputStream reader = Paths.get(filePath.toString()).toUri().toURL().openStream()) {

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(reader)
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");

            user.setGoogleCredentials(googleCredentials);

        } catch (IOException e) {

            throw new HolisticFaaSException("Error reading file: " + filePath);

        }

        JsonObject jsonFromResourcesFile = FileUtil.getJsonFromFile(filePath);

        String projectId = jsonFromResourcesFile.get("project_id").getAsString(); // TODO: error handling

        user.setProjectName(projectId);

        return user;

    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public GoogleCredentials getGoogleCredentials() {
        return googleCredentials;
    }

    public void setGoogleCredentials(GoogleCredentials googleCredentials) {
        this.googleCredentials = googleCredentials;
    }
}