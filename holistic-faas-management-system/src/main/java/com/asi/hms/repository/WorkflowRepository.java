package com.asi.hms.repository;

import com.asi.hms.model.db.DBWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkflowRepository extends JpaRepository<DBWorkflow, UUID> {
}
