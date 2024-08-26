package com.app.rally.route.controller;


import com.app.rally.route.domain.Response;
import com.app.rally.route.domain.Result;
import com.app.rally.route.security.payload.AuthenticationResponse;
import com.app.rally.route.security.payload.RegisterRequest;
import com.app.rally.route.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@Slf4j
@RequiredArgsConstructor
public class RegisterController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response<AuthenticationResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        return ResponseEntity.ok()
                .body(new Result<>(authenticationResponse));
    }

}
