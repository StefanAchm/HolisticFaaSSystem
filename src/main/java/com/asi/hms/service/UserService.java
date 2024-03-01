package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIUser;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DBUser registerUser(APIUser apiUser) {

        DBUser DBUser = new DBUser();
        DBUser.setUsername(apiUser.getUsername());

        return userRepository.save(DBUser);

    }
    
    public DBUser addCredentialsFile(APIUser apiUser) {

        throw new HolisticFaaSException("Not implemented yet");

    }

}
