package com.asi.hms.model.db;

import com.asi.hms.model.api.APIFunctionType;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "function_types")
public class DBFunctionType {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "functionType")
    private List<DBFunctionImplementation> functionImplementations;

    public static DBFunctionType fromAPIFunctionType(APIFunctionType apiFunctionType) {
        DBFunctionType dbFunctionType = new DBFunctionType();
        dbFunctionType.setName(apiFunctionType.getName());
        return dbFunctionType;
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

    public List<DBFunctionImplementation> getFunctionImplementations() {
        return functionImplementations;
    }

    public void setFunctionImplementations(List<DBFunctionImplementation> functionImplementations) {
        this.functionImplementations = functionImplementations;
    }

}
