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

    // TODO: add password
//    private String password;

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

}
