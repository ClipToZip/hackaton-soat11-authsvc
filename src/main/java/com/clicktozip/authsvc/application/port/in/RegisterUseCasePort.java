package com.clicktozip.authsvc.application.port.in;

import com.clicktozip.authsvc.adapter.in.rest.request.RegisterRequest;

public interface RegisterUseCasePort {
    void register(RegisterRequest request);
}
