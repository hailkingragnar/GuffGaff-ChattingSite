package org.web.guffgaff.guffgaff.dto;


public class LoginResponse {
    private String token;
    private String message;
    private String userName;

    // Default constructor
    public LoginResponse() {
    }

    // Constructor with all fields
    public LoginResponse(String token, String message, String userName) {
        this.token = token;
        this.message = message;
        this.userName = userName;
    }

    // Getter for token
    public String getToken() {
        return token;
    }

    // Setter for token
    public void setToken(String token) {
        this.token = token;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for userName
    public String getUserName() {
        return userName;
    }

    // Setter for userName
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
