package com.example.cyberwalletapi.dto.AccountChange;

import com.example.cyberwalletapi.enums.Roles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleChangeRequestDTO {
    private String email;
    private Roles role;
}
