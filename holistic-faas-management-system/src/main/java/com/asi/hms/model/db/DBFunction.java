package com.asi.hms.model.db;

import com.asi.hms.model.api.APIFunction2;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "functions")
public class DBFunction {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private DBWorkflow workflow;

    @ManyToOne
    @JoinColumn(name = "function_type_id")
    private DBFunctionType functionType;

    public DBFunction() {
    }

    public static DBFunction fromAPIFunction(APIFunction2 apiFunction) {
        DBFunction dbFunction = new DBFunction();
        dbFunction.setName(apiFunction.getName());
        return dbFunction;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DBWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(DBWorkflow workflow) {
        this.workflow = workflow;
    }

    public DBFunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(DBFunctionType functionType) {
        this.functionType = functionType;
    }
}
