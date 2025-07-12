package com.himusharier.ajpscrud.models;

public class AuthResponse {
    private int code;
    private String message;
    private String accessToken;
    private Object data;

    public AuthResponse() {}

    public AuthResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public AuthResponse(int code, String message, String accessToken) {
        this.code = code;
        this.message = message;
        this.accessToken = accessToken;
    }

    // Getters
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public String getAccessToken() { return accessToken; }
    public Object getData() { return data; }

    // Setters
    public void setCode(int code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setData(Object data) { this.data = data; }
}
