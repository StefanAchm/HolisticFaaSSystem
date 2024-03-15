package com.asi.hms.model.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users")
public class DBUser {

    @Id
    private String username;

    // TODO: security issue, should be stored in a secure way
    private String credentialsFilePath;

    private String provider;

    @OneToMany(mappedBy = "user")
    private List<DBFunctionDeployment> functionDeployments;

    public DBUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public void setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
