package com.asi.hms.service;

import com.asi.hms.model.db.DBUser;
import com.asi.hms.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DBUser register(String username) {

        DBUser dbUser = new DBUser();
        dbUser.setUsername(username);

        this.userRepository.save(dbUser);

        return dbUser;

    }

}
