package com.app.rally.route.service;

import com.app.rally.route.constant.AppConstant;
import com.app.rally.route.domain.*;
import com.app.rally.route.domain.Error;
import com.app.rally.route.entity.User;
import com.app.rally.route.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isAuthenticate(UserLoginDomain loginDomain) {
        User user = userRepository.getReferenceById(loginDomain.getUserName());
        log.info("user entity {}", user);
        return user.getPassword().equals(loginDomain.getPassword());
    }

    public Response doRegister(UserDomain userDomain) {

        Optional<User> optionalUserEntity = userRepository.findById(userDomain.getMobileNumber());
        log.info("data from db {}", optionalUserEntity);

        if (optionalUserEntity.isPresent()) {
            log.error("user already present into the database ");
            return new ServiceError(List.of(new Error(AppConstant.USER_ALREADY_REGISTERED, "user is already registered")));
        }

        User save = null;


        try {
           // save = userRepository.save(getUserEntity(userDomain));
        } catch (Exception e) {
            log.info("error while saving the user {}", e.getMessage());
            log.error("error details {}", e);
            return new ServiceError(List.of(new Error("ERR02", "service exception while saving user")));
        }

        return new Result(save);
    }




}


