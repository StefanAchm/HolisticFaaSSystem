package com.asi.hms.repository;

import com.asi.hms.model.db.DBFunctionImplementation;
import com.asi.hms.model.db.DBWorkflow;
import io.grpc.netty.shaded.io.netty.handler.codec.http2.Http2Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowRepository extends JpaRepository<DBWorkflow, UUID> {

}
