package com.cliptozip.authsvc.domain.model;

public class User {
    private String userId;
    private String name;
    private String email;
    private String passswordHash;

    public User() {
    }

    public User(String userId, String name, String email, String passswordHash) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passswordHash = passswordHash;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassswordHash() {
        return passswordHash;
    }

    public void setPassswordHash(String passswordHash) {
        this.passswordHash = passswordHash;
    }
}
