package com.app.rally.route.service;


import com.app.rally.route.constant.AppConstant;
import com.app.rally.route.domain.ProfileDomain;
import com.app.rally.route.entity.User;
import com.app.rally.route.exception.UserException;
import com.app.rally.route.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final FileUploadService fileUploadService;


    @Autowired
    private final JwtService jwtService;

    public User updateProfile(ProfileDomain profile) {
        Optional<User> user = userRepository.findByEmail(profile.getEmail());
        log.info("user info from db {}", user);
        User dbUser = user.orElseThrow(() -> new UserException(AppConstant.USER_NOT_FOUND, "User not found " + profile.getEmail()));

        if (profile.getFirstname() != null) {
            dbUser.setFirstname(profile.getFirstname());
        }
        if (profile.getLastname() != null) {
            dbUser.setLastname(profile.getLastname());
        }

        userRepository.save(dbUser);
        return dbUser;
    }

    public Resource getProfileImage(String userEmail, String fileName) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        log.info("user info from db {}", user);
        User dbUser = user.orElseThrow(() -> new UserException(AppConstant.USER_NOT_FOUND, "User not found " + userEmail));

        return fileUploadService.getFile(dbUser.getEmail(), fileName);
    }

    public String updateProfileImage(String userEmail, MultipartFile file) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        // log.info("user info from db {}",user);
        user.orElseThrow(() -> new UserException(AppConstant.USER_NOT_FOUND, "User not found " + userEmail));

        return fileUploadService.saveUserProfileImage(userEmail, file);
    }

    public void deleteProfile(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        log.info("deleting user {}", user);
        if (user.isPresent()) {
            jwtService.getCleanJwtCookie();
            userRepository.delete(user.get());
            log.info("user is deleted ");
            fileUploadService.removeUserProfileImage(userEmail);
        } else {
            log.info("user not found for delete {}", userEmail);
        }
    }
}
