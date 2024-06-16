package com.asi.hms.repository;

import com.asi.hms.model.db.DBUser;
import com.asi.hms.model.db.DBUserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialsRepository extends JpaRepository<DBUserCredentials, UUID> {

    Optional<DBUserCredentials> findDBUserCredentialsByUserAndProvider(DBUser user, String provider);

    Collection<DBUserCredentials> findByUser(DBUser dbUser);

    void deleteByUser(DBUser dbUser);

}
