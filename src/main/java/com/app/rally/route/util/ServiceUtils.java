package com.app.rally.route.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

public class ServiceUtils {

    public static String getToken(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // after "Bearer "
        }
        return null;
    }


}
