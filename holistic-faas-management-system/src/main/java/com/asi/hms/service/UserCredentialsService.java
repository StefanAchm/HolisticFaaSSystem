package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIUserCredentials;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.model.db.DBUserCredentials;
import com.asi.hms.repository.UserCredentialsRepository;
import com.asi.hms.repository.UserRepository;
import com.asi.hms.utils.EncryptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserCredentialsService {

    private final UserRepository userRepository;

    private final UserCredentialsRepository userCredentialsRepository;

    public UserCredentialsService(UserRepository userRepository,
                                  UserCredentialsRepository userCredentialsRepository) {

        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public String encryptFile(MultipartFile file, String key) {
        String content;
        try {
            content = new String(file.getBytes());
        } catch (IOException e) {
            throw new HolisticFaaSException(e);
        }
        return EncryptionUtil.encrypt(content, key);
    }

    public String getCredentials(UUID userId, String provider) {

        DBUser dbUser = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new HolisticFaaSException("User '" + userId + "' not found"));

        DBUserCredentials dbUserCredentials = this.userCredentialsRepository
                .findDBUserCredentialsByUserAndProvider(dbUser, provider)
                .stream()
                .findFirst()
                .orElseThrow(() -> new HolisticFaaSException("Credentials for provider '" + provider + "' not found"));

        return decryptCredentials(dbUserCredentials.getCredentialsFile(), dbUser.getEncryptionKey());

    }

    public String decryptCredentials(String encryptedCredentials, String key) {
        return EncryptionUtil.decrypt(encryptedCredentials, key);
    }

    public APIUserCredentials addOrUpdate(MultipartFile file,
                                          APIUserCredentials apiUserCredentials,
                                          boolean alwaysCreate) {

        DBUser dbUser = this.userRepository
                .findById(apiUserCredentials.getUserId())
                .orElseThrow(() -> new HolisticFaaSException("User '" + apiUserCredentials.getUserId() + "' not found"));

        String encryptedFiled = encryptFile(file, dbUser.getEncryptionKey());

        DBUserCredentials dbUserCredentials = new DBUserCredentials();

        if(!alwaysCreate) {

            dbUserCredentials = this.userCredentialsRepository.findDBUserCredentialsByUserAndProvider(
                            dbUser,
                            apiUserCredentials.getProvider()
                    )
                    .stream()
                    .findFirst()
                    .orElseGet(DBUserCredentials::new);

        }

        dbUserCredentials.setUser(dbUser);
        dbUserCredentials.setProvider(apiUserCredentials.getProvider());
        dbUserCredentials.setCredentialsFile(encryptedFiled);

        this.userCredentialsRepository.save(dbUserCredentials);

        return APIUserCredentials.fromDBUserCredentials(dbUserCredentials);

    }

    public List<APIUserCredentials> getAllUser() {
        return this.userCredentialsRepository.findAll().stream().map(APIUserCredentials::fromDBUserCredentials).toList();
    }

    public List<APIUserCredentials> get(UUID userId) {

        DBUser dbUser = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new HolisticFaaSException("User '" + userId + "' not found"));

        return this.userCredentialsRepository.findByUser(dbUser)
                .stream()
                .map(APIUserCredentials::fromDBUserCredentials)
                .toList();

    }



}
