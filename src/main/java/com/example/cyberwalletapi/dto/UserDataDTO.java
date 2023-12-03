package com.example.cyberwalletapi.dto;
import com.example.cyberwalletapi.enums.Roles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataDTO {
    private String name;
    private String email;
    private String address;
    private Double balance;
}
