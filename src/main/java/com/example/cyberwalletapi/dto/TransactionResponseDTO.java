package com.example.cyberwalletapi.dto;

import com.example.cyberwalletapi.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private String id;
    private String recipient;
    private String amount;
    private LocalDateTime dateOfTransaction;
    private String description;

}
