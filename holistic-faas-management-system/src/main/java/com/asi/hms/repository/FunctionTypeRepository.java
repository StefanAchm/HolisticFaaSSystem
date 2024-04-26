package com.asi.hms.repository;

import com.asi.hms.model.db.DBFunctionType;
import io.grpc.netty.shaded.io.netty.handler.codec.http2.Http2Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FunctionTypeRepository extends JpaRepository<DBFunctionType, UUID> {

    @Query("SELECT ft FROM DBFunctionType ft " +
            "JOIN ft.functions f " +
            "WHERE f.workflow.id = :workflowId")
    List<DBFunctionType> findByFunctionWorkflowId(@Param("workflowId") UUID workflowId);
}
