package com.asi.hms.repository;

import com.asi.hms.model.db.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<DBUser, UUID> {
    DBUser findByUsername(String username);
}
