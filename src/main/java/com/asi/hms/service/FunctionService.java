package com.asi.hms.service;

import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.db.DBFunction;
import com.asi.hms.repository.FunctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionService {

    private final FunctionRepository functionRepository;

    public FunctionService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public void addFunction(APIFunction apiFunction) {

        DBFunction dbFunction = new DBFunction();

        dbFunction.setFilePath(apiFunction.getFilePath());
        dbFunction.setName(apiFunction.getName());

        functionRepository.save(dbFunction);

    }

    public List<APIFunction> getAllFunction() {

        return this.functionRepository.findAll().stream().map(APIFunction::fromDBFunction).toList();

    }

}
