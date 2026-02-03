package com.cliptozip.authsvc.application.port.in;

import com.cliptozip.authsvc.adapter.in.rest.request.RegisterRequest;

public interface RegisterUseCasePort {
    void register(RegisterRequest request);
}
