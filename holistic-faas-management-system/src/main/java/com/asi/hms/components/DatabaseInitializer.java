package com.asi.hms.components;

import com.asi.hms.repository.FunctionDeploymentRepository;
import com.asi.hms.repository.FunctionImplementationRepository;
import com.asi.hms.repository.FunctionTypeRepository;
import com.asi.hms.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final FunctionDeploymentRepository functionDeploymentRepository;
    private final FunctionImplementationRepository functionImplementationRepository;
    private final FunctionTypeRepository functionTypeRepository;
    private final UserRepository userRepository;

    public DatabaseInitializer(FunctionDeploymentRepository functionDeploymentRepository, FunctionImplementationRepository functionImplementationRepository, FunctionTypeRepository functionTypeRepository, UserRepository userRepository) {
        this.functionDeploymentRepository = functionDeploymentRepository;
        this.functionImplementationRepository = functionImplementationRepository;
        this.functionTypeRepository = functionTypeRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init() {

        // todo

    }

}
