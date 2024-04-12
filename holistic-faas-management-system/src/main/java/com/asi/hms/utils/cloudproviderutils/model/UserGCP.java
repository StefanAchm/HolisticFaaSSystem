package com.asi.hms.utils.cloudproviderutils.model;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.FileUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonElement;
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

        JsonElement projectId = jsonFromResourcesFile.get("project_id");

        if (projectId == null) {
            throw new HolisticFaaSException("project_id not found in file: " + filePath);
        }

        if (!projectId.isJsonPrimitive() || !projectId.getAsJsonPrimitive().isString()) {
            throw new HolisticFaaSException("project_id is not a string in file: " + filePath);
        }

        String projectIdAsString = projectId.getAsString();

        user.setProjectName(projectIdAsString);

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