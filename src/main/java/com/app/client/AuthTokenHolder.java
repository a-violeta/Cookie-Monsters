package com.app.client;

import org.springframework.stereotype.Component;

@Component
public class AuthTokenHolder {

    private volatile String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void clear() {
        this.token = null;
    }
}
