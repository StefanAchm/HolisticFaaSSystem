package com.asi.hms.repository;

import com.asi.hms.model.db.DBFunctionImplementation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FunctionImplementationRepository extends JpaRepository<DBFunctionImplementation, UUID> {

    @Query("SELECT fi FROM DBFunctionImplementation fi " +
            "JOIN fi.functionType ft " +
            "JOIN ft.functions f " +
            "WHERE f.workflow.id = :workflowId")
    List<DBFunctionImplementation> findByFunctionWorkflowId(@Param("workflowId") UUID workflowId);

}
