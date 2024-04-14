package com.asi.hms.service;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIUserCredentials;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.model.db.DBUserCredentials;
import com.asi.hms.repository.UserCredentialsRepository;
import com.asi.hms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserCredentialsService {

    private final UserRepository userRepository;

    private final UserCredentialsRepository userCredentialsRepository;

    private final UploadFileService uploadFileService;

    public UserCredentialsService(UserRepository userRepository,
                                  UserCredentialsRepository userCredentialsRepository,
                                  UploadFileService uploadFileService) {

        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.uploadFileService = uploadFileService;

    }

    public void add(MultipartFile file, APIUserCredentials apiUserCredentials) {

        DBUser dbUser = this.userRepository
                .findById(apiUserCredentials.getUserId())
                .orElseThrow(() -> new HolisticFaaSException("User '" + apiUserCredentials.getUserId() + "' not found"));

        String path = this.uploadFileService.uploadFileAndNormalize(file, UploadFileService.CREDENTIALS_DIR);

        DBUserCredentials dbUserCredentials = new DBUserCredentials();
        dbUserCredentials.setUser(dbUser);
        dbUserCredentials.setProvider(apiUserCredentials.getProvider());
        dbUserCredentials.setCredentialsFilePath(path);

        this.userCredentialsRepository.save(dbUserCredentials);

    }

    public List<APIUserCredentials> getAllUser() {
        return this.userCredentialsRepository.findAll().stream().map(APIUserCredentials::fromDBUser).toList();
    }
}
