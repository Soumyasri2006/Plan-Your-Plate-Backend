
package com.planyourplate.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @GetMapping
    public Map<String, String> getProfile(Authentication authentication) {
        return Map.of(
            "message", "JWT authentication successful!",
            "email", authentication.getName()
        );
    }
}


