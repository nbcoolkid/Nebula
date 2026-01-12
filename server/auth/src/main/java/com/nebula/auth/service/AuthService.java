package com.nebula.auth.service;

import com.nebula.auth.dto.request.LoginRequest;
import com.nebula.auth.dto.response.LoginResponse;
import com.nebula.auth.dto.response.UserInfoResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    UserInfoResponse getUserInfo();
}
