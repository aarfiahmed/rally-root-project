package com.app.rally.route.controller;


import com.app.rally.route.service.ClubService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ClubManagerController {
private final ClubService service;


@PostMapping("/api/v1/auth/clubs")
    public void addClub(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        log.info("adding club info");
        service.addClub(file);
    }


    @GetMapping("/api/v1/auth/clubs/template")
    public ResponseEntity<Resource> getClubTemplate(HttpServletRequest request){
        log.info("inside getClubTemplate");
        Resource resource = service.getClubTemplate();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
