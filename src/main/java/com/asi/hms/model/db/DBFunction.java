package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "functions")
public class DBFunction {

    @Id
    @GeneratedValue
    private UUID id;

    private String provider;
    private String filePath;
    private String name;
    private Integer memory;
    private Integer timeoutSecs;
    private String handler;
    private String region;
    private String runtime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DBUser DBUser;

    public DBFunction() {
    }

    public UUID getId() {
        return id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getTimeoutSecs() {
        return timeoutSecs;
    }

    public void setTimeoutSecs(Integer timeoutSecs) {
        this.timeoutSecs = timeoutSecs;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public DBUser getUser() {
        return DBUser;
    }

    public void setUser(DBUser DBUser) {
        this.DBUser = DBUser;
    }
}
