package com.example.cyberwalletapi.dto.AccountChange;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequestDTO {
    private String password;
    private String email;
}
