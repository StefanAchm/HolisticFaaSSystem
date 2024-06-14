package com.asi.hms.model;

import com.asi.hms.utils.FileUtil;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

public class UserAWS implements UserInterface {

    private static final String ROLE_NAME = "lambda-execution-role-hf";
    private static final String ROLE_NAME_ACADEMY = "LabRole";

    private String accessKeyId;
    private String secretAccessKey;
    private String sessionToken;

    private String roleArn;

    public static UserAWS fromInputStream(InputStream inputStream) {

        Properties properties = FileUtil.getPropertiesFromInputStream(inputStream);

        UserAWS user = new UserAWS();
        user.setAccessKeyId(properties.getProperty("aws.accessKeyId"));
        user.setSecretAccessKey(properties.getProperty("aws.secretAccessKey"));

        user.setRoleArn(properties.getProperty("aws.roleArn"));

        user.setSessionToken(properties.getProperty("aws.sessionToken"));

        return user;

    }

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

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public boolean isWithSessionToken() {
        return this.sessionToken != null && !this.sessionToken.isEmpty();
    }

    public String getRoleName() {
        return this.isWithSessionToken() ? ROLE_NAME_ACADEMY : ROLE_NAME;
    }

    public AwsCredentials getAwsCredentials() {

        if (this.isWithSessionToken()) {

            return AwsSessionCredentials.create(
                    this.getAccessKeyId(),
                    this.getSecretAccessKey(),
                    this.getSessionToken()
            );

        } else {

            return AwsBasicCredentials.create(
                    this.getAccessKeyId(),
                    this.getSecretAccessKey()
            );

        }

    }

}
