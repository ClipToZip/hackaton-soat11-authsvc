package com.clicktozip.authsvc.application.port.out;

import java.util.concurrent.TimeUnit;

public interface TokenCachePort {
    void cacheToken(String key, String token, long timeout, TimeUnit unit);
    String getToken(String key);
    void deleteToken(String key);
}
