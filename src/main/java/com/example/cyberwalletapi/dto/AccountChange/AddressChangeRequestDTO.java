package com.example.cyberwalletapi.dto.AccountChange;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressChangeRequestDTO {
    private String address;
    private String email;
}
