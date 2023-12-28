package com.example.cyberwalletapi.dto.AccountChange;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailChangeRequestDTO {
    private String newEmail;
    private String oldEmail;
}
