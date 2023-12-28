package com.example.cyberwalletapi.dto.AccountChange;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceChangeRequestDTO {
    private Double balance;
    private String email;
}
