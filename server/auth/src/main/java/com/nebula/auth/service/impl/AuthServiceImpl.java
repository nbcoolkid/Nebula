package com.nebula.auth.service.impl;

import com.nebula.auth.dto.request.LoginRequest;
import com.nebula.auth.dto.response.LoginResponse;
import com.nebula.auth.dto.response.UserInfoResponse;
import com.nebula.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginResponse login(LoginRequest request) {
        log.debug("Processing login request for user: {}", request.getUserName());

        String token = "Bearer " + UUID.randomUUID().toString().replace("-", "");
        String refreshToken = "Refresh-" + UUID.randomUUID().toString().replace("-", "");

        log.debug("Login successful for user: {}", request.getUserName());
        return new LoginResponse(token, refreshToken);
    }

    @Override
    public UserInfoResponse getUserInfo() {
        log.debug("Fetching user info");

        UserInfoResponse response = new UserInfoResponse();
        response.setUserId(1);
        response.setUserName("admin");
        response.setEmail("admin@nebula.com");
        response.setAvatar("https://ui-avatars.com/api/?name=Admin&background=0D8ABC&color=fff");
        response.setRoles(Arrays.asList("R_SUPER", "R_ADMIN"));
        response.setButtons(Arrays.asList("add", "edit", "delete"));
        return response;
    }
}
