package com.asi.hms.service;

import com.asi.hms.model.api.APIUser;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.model.db.UserPrincipal;
import com.asi.hms.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

    }

    public DBUser register(APIUser user) {

        DBUser dbUser = new DBUser();
        dbUser.setUsername(user.getUsername());
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));

        this.userRepository.save(dbUser);

        return dbUser;

    }

//    public String login(String username) {
//
//        Authentication authentication = authenticationManager
//                .authenticate(
//
//
////        DBUser dbUser = this.userRepository.findByUsername(username);
////
////        if (dbUser == null) {
////            return "User not found";
////        }
////
////        return "User found";
//
//
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        DBUser dbUser = this.userRepository.findByUsername(username);

        if (dbUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserPrincipal(dbUser);

    }
}
