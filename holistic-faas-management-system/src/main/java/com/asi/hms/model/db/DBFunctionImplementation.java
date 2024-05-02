package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "function_implementations")
public class DBFunctionImplementation {

    @Id
    @GeneratedValue
    private UUID id;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "function_type_id")
    private DBFunctionType functionType;

    @OneToMany(mappedBy = "functionImplementation")
    private List<DBFunctionDeployment> functionDeployments;

    public DBFunctionImplementation() {
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

    public List<DBFunctionDeployment> getFunctionDeployments() {
        return functionDeployments;
    }

    public void setFunctionDeployments(List<DBFunctionDeployment> functionDeployments) {
        this.functionDeployments = functionDeployments;
    }

    public DBFunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(DBFunctionType functionType) {
        this.functionType = functionType;
    }



}
