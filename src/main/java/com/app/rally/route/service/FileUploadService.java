package com.app.rally.route.service;


import com.app.rally.route.constant.AppConstant;
import com.app.rally.route.exception.FileProcessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileUploadService {
    private final Path userProfilePath;
    private final Path clubTemplatePath;
    private final Path clubImagePath;

    public FileUploadService() {
        userProfilePath = Paths.get("files/userProfileLocation").toAbsolutePath().normalize();
        clubTemplatePath = Paths.get("files/clubTemplate").toAbsolutePath().normalize();
        clubImagePath=  Paths.get("files/club").toAbsolutePath().normalize();
    }

    public String saveUserProfileImage(String email, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Files.createDirectory(userProfilePath.resolve(email));
        } catch (IOException e) {
            try {
                File[] files = userProfilePath.resolve(email).toFile().listFiles();
                for (File f : files) {
                    f.delete();
                }
            } catch (RuntimeException ex) {
                log.info("error in creating folder {}", ex.getMessage());
                throw new FileProcessException("Could not create folder " + ex.getMessage(), AppConstant.FILE_UPLOAD_ERROR);
            }

            log.info("folder already present {}", e.getMessage());
        }

        try {
            Path targetLocation = this.userProfilePath.resolve(email + "/" + fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            log.info("error while saving file {}", ex.getMessage());
            throw new FileProcessException("Could not store file " + ex.getMessage(), AppConstant.FILE_UPLOAD_ERROR);
        }
        return fileName;
    }

    public boolean isProfileImagePresent(String userEmail) {

        Path filePath = this.userProfilePath.resolve(userEmail).normalize();
        File[] files = filePath.toFile().listFiles();
        return (files != null && files.length>0);

    }

    public void removeUserProfileImage(String email) {
        Path filePath = this.userProfilePath.resolve(email).normalize();
        File[] files = filePath.toFile().listFiles();
        if (files != null) {
            log.info("user profile image is present {}", email);
            for(File file: files) {
                log.info("user profile image name {} and delete status {}", file.getName(),file.delete());
            }
           log.info("user folder delete status{}",filePath.toFile().delete());
        } else {
            log.info("profile image not present for user {}", email);
        }
    }

    public Resource getFile(String userEmail, String fileName) {
        try {
            Path filePath = this.userProfilePath.resolve(userEmail + "/" + fileName).normalize();
            if (!filePath.toFile().exists()) {
                throw new FileProcessException("No profile present for user " + userEmail, AppConstant.PROFILE_IMAGE_NOT_FOUND);
            }
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileProcessException("File not found ", AppConstant.FILE_NOT_FOUND_ERROR);
            }
        } catch (MalformedURLException ex) {
            throw new FileProcessException("File processing error" + ex.getMessage(), AppConstant.FILE_NOT_FOUND_ERROR);
        }
    }

    public String getUserProfileImageName(String userEmail) {
        Path filePath = this.userProfilePath.resolve(userEmail).normalize();
        File[] files = filePath.toFile().listFiles();
        if(files !=null && files.length>0){
            return files[0].getName();
        }
      return null;
    }

    public void saveClubTemplate(MultipartFile file) {
        log.info("saving club template");

        try {
            Path targetLocation = this.clubTemplatePath.resolve(StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            log.error("error while saving club template ", ex);
            throw new FileProcessException("Could not save club template " + ex.getMessage(), AppConstant.FILE_UPLOAD_ERROR);
        }
    }

    public Resource getClubTemplate() {
        log.info("getting the club template");
        File[] files = clubTemplatePath.toFile().listFiles();
        try {
            if (files != null && new UrlResource(files[0].toURI()).exists()) {
                log.info("club template found and returning the resource");
                return new UrlResource(files[0].toURI());
            }
        } catch (MalformedURLException e) {
            throw new FileProcessException("File not found " + e.getMessage(), AppConstant.FILE_NOT_FOUND_ERROR);
        }
        throw new FileProcessException("File not found ", AppConstant.FILE_NOT_FOUND_ERROR);
    }

    public void saveClubImage(byte[] data,String name) throws IOException {
        log.info("saving club image for club {}",name);
        Path target = clubImagePath.resolve(name + ".jpg" );
        Files.copy(new ByteArrayInputStream(data), target, StandardCopyOption.REPLACE_EXISTING);
        log.info("club image is saved {}",name);
    }

    public String getClubImageName(String clubName) {
        return this.clubImagePath.resolve(clubName + ".jpg").normalize().toFile().getName();
    }

    public Resource getClubImageByName( String fileName) {
        try {
            Path filePath = this.clubImagePath.resolve( fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                log.info("club image found for club {}",fileName);
                return resource;
            } else {
                throw new FileProcessException("File not found "+fileName, AppConstant.FILE_NOT_FOUND_ERROR);
            }
        } catch (MalformedURLException ex) {
            throw new FileProcessException("File processing error" + ex.getMessage(), AppConstant.FILE_NOT_FOUND_ERROR);
        }
    }
}
