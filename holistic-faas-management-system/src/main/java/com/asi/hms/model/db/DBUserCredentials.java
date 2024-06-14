package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_credentials")
public class DBUserCredentials {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String credentialsFile;

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

    public String getCredentialsFile() {
        return credentialsFile;
    }

    public void setCredentialsFile(String credentialsFile) {
        this.credentialsFile = credentialsFile;
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
