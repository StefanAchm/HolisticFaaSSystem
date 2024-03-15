package com.asi.hms.service;

import com.asi.hms.model.api.APIUser;
import com.asi.hms.model.db.DBUser;
import com.asi.hms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UploadFileService uploadFileService;

    public UserService(UserRepository userRepository, UploadFileService uploadFileService) {
        this.userRepository = userRepository;
        this.uploadFileService = uploadFileService;
    }

    public void create(MultipartFile file, APIUser apiUser) {

        String path = this.uploadFileService.uploadFileAndNormalize(file, UploadFileService.CREDENTIALS_DIR);

        DBUser dbUser = new DBUser();
        dbUser.setUsername(apiUser.getUsername());
        dbUser.setProvider(apiUser.getProvider());
        dbUser.setCredentialsFilePath(path);

        this.userRepository.save(dbUser);

    }

    public List<APIUser> getAllUser() {
        return this.userRepository.findAll().stream().map(APIUser::fromDBUser).toList();
    }
}
