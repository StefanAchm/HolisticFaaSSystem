package com.asi.hms.model.db;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class DBUser {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<DBUserCredentials> userCredentials;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<DBUserCredentials> getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(List<DBUserCredentials> userCredentials) {
        this.userCredentials = userCredentials;
    }
}
