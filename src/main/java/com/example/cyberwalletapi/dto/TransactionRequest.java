package com.example.cyberwalletapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class TransactionRequest {
    private String recipient;
    private Double amount;
    private LocalDateTime dateOfTransaction;
    private String description;



}
