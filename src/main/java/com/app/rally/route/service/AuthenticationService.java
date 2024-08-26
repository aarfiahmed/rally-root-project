package com.app.rally.route.service;

import com.app.rally.route.security.payload.AuthenticationRequest;
import com.app.rally.route.security.payload.AuthenticationResponse;
import com.app.rally.route.security.payload.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
