package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.SignUpRequest;
import com.example.cyberwalletapi.dto.TransactionRequest;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.services.UserTransactionService;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class UserTransactionController {
    private final UserTransactionService userTransactionService;
    public UserTransactionController(UserTransactionService userTransactionService) {
        this.userTransactionService = userTransactionService;
    }

}
