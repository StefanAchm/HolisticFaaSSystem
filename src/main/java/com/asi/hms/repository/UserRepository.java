package com.asi.hms.repository;

import com.asi.hms.model.db.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DBUser, String> {

}
