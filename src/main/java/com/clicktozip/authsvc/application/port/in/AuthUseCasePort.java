package com.clicktozip.authsvc.application.port.in;

import com.clicktozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.clicktozip.authsvc.adapter.in.rest.response.TokenResponse;

public interface AuthUseCasePort {
    TokenResponse login(LoginRequest request);
}
