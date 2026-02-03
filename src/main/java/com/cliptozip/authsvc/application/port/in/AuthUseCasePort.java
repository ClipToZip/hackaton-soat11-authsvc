package com.cliptozip.authsvc.application.port.in;

import com.cliptozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.cliptozip.authsvc.adapter.in.rest.response.TokenResponse;

public interface AuthUseCasePort {
    TokenResponse login(LoginRequest request);
}
