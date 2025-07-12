package com.himusharier.ajpscrud.models;

public class AuthRegisterLoginRequest {
    private String email;
    private String password;
    private String userRole;

    public AuthRegisterLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.userRole = "USER";
    }

    public AuthRegisterLoginRequest(String email, String password, String userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole != null ? userRole : "USER";
    }

    // Getters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUserRole() { return userRole; }

    // Setters
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
}
