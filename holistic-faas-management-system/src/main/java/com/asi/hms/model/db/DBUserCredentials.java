package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_credentials")
public class DBUserCredentials {

    @Id
    @GeneratedValue
    private UUID id;

    private String credentialsFilePath; // TODO: security issue, should be stored in a secure way

    private String provider;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DBUser user;

    public DBUserCredentials() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public DBUser getUser() {
        return user;
    }

    public void setUser(DBUser user) {
        this.user = user;
    }
    
}
