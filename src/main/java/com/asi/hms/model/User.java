package com.asi.hms.model;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;

public class User {

    private String accessKeyId;
    private String secretAccessKey;

    private GoogleCredentials googleCredentials;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public GoogleCredentials getGoogleCredentials() {
        return googleCredentials;
    }

    public void setGoogleCredentialsFromInputStream(InputStream inputStream) throws HolisticFaaSException {

        try {

            this.googleCredentials = GoogleCredentials
                    .fromStream(inputStream)
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");

        } catch (IOException e) {

            throw new HolisticFaaSException(e.getMessage());

        }

    }

}
