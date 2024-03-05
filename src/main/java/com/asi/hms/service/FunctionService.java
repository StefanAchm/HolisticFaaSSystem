package com.asi.hms.service;

import com.asi.hms.enums.Provider;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.repository.FunctionRepository;
import com.asi.hms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

        DBUser user = userRepository.findByUsername(apiFunction.getUserName()); // TODO:

        if(user == null) {
            throw new HolisticFaaSException("User '" + apiFunction.getUserName() + "' not found");
        }

        dbFunction.setUser(user);

        functionRepository.save(dbFunction);

    }

    public List<APIFunction> getAllFunction() {

        return this.functionRepository.findAll().stream().map(dbFunction -> {

            APIFunction apiFunction = new APIFunction();

            apiFunction.setProvider(Provider.valueOf(dbFunction.getProvider()));
            apiFunction.setFilePath(dbFunction.getFilePath());
            apiFunction.setName(dbFunction.getName());
            apiFunction.setMemory(dbFunction.getMemory());
            apiFunction.setTimeoutSecs(dbFunction.getTimeoutSecs());
            apiFunction.setHandler(dbFunction.getHandler());
            apiFunction.setRegion(dbFunction.getRegion());

            return apiFunction;

        }).toList();


    }

}
