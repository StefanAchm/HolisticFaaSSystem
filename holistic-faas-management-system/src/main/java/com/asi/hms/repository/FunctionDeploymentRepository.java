package com.asi.hms.repository;

import com.asi.hms.model.db.DBFunctionDeployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FunctionDeploymentRepository extends JpaRepository<DBFunctionDeployment, UUID> {

    @Query("SELECT fd FROM DBFunctionDeployment fd " +
            "JOIN fd.workflowDeployment wd " +
            "WHERE wd.id = :workflowDeploymentId")
    List<DBFunctionDeployment> findAllByWorkflowDeploymentId(@Param("workflowDeploymentId") UUID workflowDeploymentId);

}
