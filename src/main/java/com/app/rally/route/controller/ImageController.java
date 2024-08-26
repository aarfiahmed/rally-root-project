package com.app.rally.route.controller;

import com.app.rally.route.constant.AppConstant;
import com.app.rally.route.domain.Result;
import com.app.rally.route.domain.UploadFileResponse;
import com.app.rally.route.service.FileUploadService;
import com.app.rally.route.service.JwtService;
import com.app.rally.route.service.ProfileService;
import com.app.rally.route.util.ServiceUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageController {
    private final JwtService jwtService;
    private final ProfileService profileService;
    private final FileUploadService fileUploadService;

    @GetMapping(AppConstant.GET_PROFILE_URI+"{fileName}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String fileName,@RequestParam String email ,HttpServletRequest request){
        log.info("Getting the profile image of user  {} and its image {}",email,fileName);
        Resource resource = profileService.getProfileImage(email,fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }


    @PostMapping("/api/v1/resource/users/profile/image")
    public Result<UploadFileResponse> updateProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String jwt = ServiceUtils.getToken(request);
        final String userEmail =jwtService.extractUserName(jwt);
        String uploadedFileName = profileService.updateProfileImage(userEmail, file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AppConstant.GET_PROFILE_URI)
                .path(uploadedFileName)
                .queryParam("email",userEmail)
                .toUriString();

        return new Result<>(new UploadFileResponse(uploadedFileName, fileDownloadUri,
                file.getContentType(), file.getSize()));
    }


    @GetMapping(AppConstant.GET_CLUB_IMAGE_URI+"{fileName}")
    public ResponseEntity<Resource>  downloadClubImage(@PathVariable String fileName){
        log.info("in imageController and getting club image by name {}",fileName);
        Resource clubImageByName = fileUploadService.getClubImageByName(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + clubImageByName.getFilename() + "\"")
                .body(clubImageByName);

    }
}
