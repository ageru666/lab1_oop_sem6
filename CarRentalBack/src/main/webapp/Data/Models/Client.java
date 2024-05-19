package main.webapp.Data.Models;

import lombok.Data;

@Data
public class Client {
    private int id;
    private String fullName;
    private String passportNumber;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private String role;
}

