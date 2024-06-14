package com.asi.hms.service;

import com.asi.hms.model.api.APILoginResponse;
import com.asi.hms.model.api.APIUser;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.model.db.UserPrincipal;
import com.asi.hms.repository.UserRepository;
import com.asi.hms.utils.EncryptionUtil;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

    }


    public APILoginResponse login(APIUser user, String token) {

        UserPrincipal userPrincipal = this.loadUserByUsername(user.getUsername());
        APIUser apiUserFromDb = APIUser.fromDBUser(userPrincipal.getDbUser());

        return new APILoginResponse(apiUserFromDb, token);

    }



    public APIUser register(APIUser user) {

        DBUser dbUser = new DBUser();
        dbUser.setUsername(user.getUsername());
        dbUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        dbUser.setEncryptionKey(EncryptionUtil.generateKey());

        this.userRepository.save(dbUser);

        return APIUser.fromDBUser(dbUser);

    }

    public List<APIUser> getAllUser() {

        List<DBUser> dbUsers = this.userRepository.findAll();

        return dbUsers.stream().map(APIUser::fromDBUser).toList();

    }


    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

        DBUser dbUser = this.userRepository.findByUsername(username);

        if (dbUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserPrincipal(dbUser);

    }

    public boolean checkIfUserExists(String username) {
        return this.userRepository.findByUsername(username) != null;
    }

}
