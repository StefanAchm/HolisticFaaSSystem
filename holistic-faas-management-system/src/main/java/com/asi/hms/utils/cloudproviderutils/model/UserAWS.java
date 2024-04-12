package com.asi.hms.utils.cloudproviderutils.model;

import com.asi.hms.utils.FileUtil;

import java.nio.file.Path;
import java.util.Properties;

public class UserAWS implements UserInterface {

    private String accessKeyId;
    private String secretAccessKey;

    private String roleArn;

    public static UserAWS fromFile(Path filePath) {

        Properties properties = FileUtil.getPropertiesFromFile(filePath);

        UserAWS user = new UserAWS();
        user.setAccessKeyId(properties.getProperty("aws.accessKeyId"));
        user.setSecretAccessKey(properties.getProperty("aws.secretAccessKey"));
        user.setRoleArn(properties.getProperty("aws.roleArn"));

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


}
