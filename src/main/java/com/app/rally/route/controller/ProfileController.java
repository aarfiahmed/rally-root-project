package com.app.rally.route.controller;

import com.app.rally.route.constant.AppConstant;
import com.app.rally.route.domain.ProfileDomain;
import com.app.rally.route.domain.Result;
import com.app.rally.route.entity.User;
import com.app.rally.route.service.FileUploadService;
import com.app.rally.route.service.JwtService;
import com.app.rally.route.service.ProfileService;
import com.app.rally.route.util.ServiceUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/resource/users")
public class ProfileController {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final FileUploadService fileUploadService;

    private final ProfileService profileService;


    @GetMapping( )
    public Result<UserDetails> getProfile(HttpServletRequest request){
        String jwt = ServiceUtils.getToken(request);
        final String userEmail =jwtService.extractUserName(jwt);
        log.info("user email from token {}",userEmail);
        User userDetails = (User)this.userDetailsService.loadUserByUsername(userEmail);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AppConstant.GET_PROFILE_URI)
                .path(fileUploadService.getUserProfileImageName(userEmail))
                .queryParam("email",userEmail)
                .toUriString();

        userDetails.setProfileImage(fileUploadService.isProfileImagePresent(userEmail)?fileDownloadUri:null);

      return new Result<>(userDetails);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest request){
        String jwt = ServiceUtils.getToken(request);
        final String userEmail =jwtService.extractUserName(jwt);
        log.info("inside delete profile and user name {}", userEmail);
        profileService.deleteProfile(userEmail);
           return ResponseEntity.ok().build();
    }

    @PutMapping()
    public Result<UserDetails> updateProfile(@RequestBody ProfileDomain profile){
        User user = profileService.updateProfile(profile);
        return new Result<>( user);
    }



}
