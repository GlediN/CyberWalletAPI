package com.example.cyberwalletapi.dto;

import com.example.cyberwalletapi.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class TransactionRequest {
    private String recipient;
    private Double amount;
    private String description;
    private String email;
}
