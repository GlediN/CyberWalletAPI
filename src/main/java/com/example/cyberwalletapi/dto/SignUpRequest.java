package com.example.cyberwalletapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String contactNumber;
    private String city;
    private String country;
    private String address;
    private String subscription;
}
