package com.asi.hms.model;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.FileUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserGCP implements UserInterface {

    private String projectName;
    private GoogleCredentials googleCredentials;

    public static UserGCP fromInputStream(InputStream inputStream) {

        try {

            UserGCP user = new UserGCP();

            byte[] bytes = inputStream.readAllBytes();

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ByteArrayInputStream(bytes))
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");

            user.setGoogleCredentials(googleCredentials);

            JsonObject jsonFromResourcesFile = FileUtil.getJsonFromInputStream(new ByteArrayInputStream(bytes));

            JsonElement projectId = jsonFromResourcesFile.get("project_id");

            if (projectId == null) {
                throw new HolisticFaaSException("project_id not found");
            }

            if (!projectId.isJsonPrimitive() || !projectId.getAsJsonPrimitive().isString()) {
                throw new HolisticFaaSException("project_id is not a string");
            }

            String projectIdAsString = projectId.getAsString();

            user.setProjectName(projectIdAsString);

            return user;

        } catch (IOException e) {

            throw new HolisticFaaSException("Error reading input stream");

        }

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