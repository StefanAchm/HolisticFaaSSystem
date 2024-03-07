package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "functions")
public class DBFunction {

    @Id
    @GeneratedValue
    private UUID id;

    private String filePath;
    private String name;

    @OneToMany(mappedBy = "function")
    private List<DBFunctionDeployment> functionDeployments;

    public DBFunction() {
    }

    public UUID getId() {
        return id;
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

    public List<DBFunctionDeployment> getFunctionDeployments() {
        return functionDeployments;
    }

    public void setFunctionDeployments(List<DBFunctionDeployment> functionDeployments) {
        this.functionDeployments = functionDeployments;
    }

}
