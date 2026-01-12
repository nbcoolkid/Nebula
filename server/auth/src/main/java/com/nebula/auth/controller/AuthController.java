package com.nebula.auth.controller;

import com.nebula.auth.dto.request.LoginRequest;
import com.nebula.auth.dto.response.LoginResponse;
import com.nebula.auth.dto.response.UserInfoResponse;
import com.nebula.auth.service.AuthService;
import com.nebula.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Processing login request for user: {}", request.getUserName());
        LoginResponse response = authService.login(request);
        return Result.success(response, "Login successful");
    }

    @GetMapping("/user/info")
    public Result<UserInfoResponse> getUserInfo() {
        log.info("Fetching user info");
        UserInfoResponse response = authService.getUserInfo();
        return Result.success(response);
    }
}
