package com.asi.hms.service;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.repository.FunctionRepository;
import com.asi.hms.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FunctionService {

    private final FunctionRepository functionRepository;

    private final UserRepository userRepository;

    public FunctionService(FunctionRepository functionRepository, UserRepository userRepository) {
        this.functionRepository = functionRepository;
        this.userRepository = userRepository;
    }

    public void addFunction(APIFunction apiFunction) {

        DBFunction dbFunction = new DBFunction();

        dbFunction.setProvider(apiFunction.getProvider().toString());
        dbFunction.setFilePath(apiFunction.getFilePath());
        dbFunction.setName(apiFunction.getName());
        dbFunction.setMemory(apiFunction.getMemory());
        dbFunction.setTimeoutSecs(apiFunction.getTimeoutSecs());
        dbFunction.setHandler(apiFunction.getHandler());
        dbFunction.setRegion(apiFunction.getRegion());

        dbFunction.setUser();

        functionRepository.save(dbFunction);


    }

}
