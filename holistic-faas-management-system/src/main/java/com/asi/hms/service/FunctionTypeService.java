package com.asi.hms.service;

import com.asi.hms.model.api.APIFunctionType;
import com.asi.hms.model.db.DBFunctionType;
import com.asi.hms.repository.FunctionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionTypeService {

    private final FunctionTypeRepository functionTypeRepository;

    public FunctionTypeService(FunctionTypeRepository functionTypeRepository) {
        this.functionTypeRepository = functionTypeRepository;
    }

    public void addFunctionType(APIFunctionType apiFunctionType) {

        DBFunctionType dbFunctionType = DBFunctionType.fromAPIFunctionType(apiFunctionType);

        this.functionTypeRepository.save(dbFunctionType);

    }

    public List<APIFunctionType> getAllFunctionTypes() {

        return this.functionTypeRepository.findAll().stream().map(APIFunctionType::fromDBFunctionType).toList();

    }


}
