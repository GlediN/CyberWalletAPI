package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.TransactionRequest;
import com.example.cyberwalletapi.services.UserTransactionService;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserTransactionController {
    private final UserTransactionService userTransactionService;

    public UserTransactionController(UserTransactionService userTransactionService) {
        this.userTransactionService = userTransactionService;
    }
    @PostMapping(path = "/transaction")
    public ResponseEntity<String> saveTransaction(@RequestBody TransactionRequest transactionRequest, @RequestHeader("Authorization") String token) {
        try {
            return userTransactionService.userTransaction(transactionRequest,token);
        } catch (Exception e) {
            e.printStackTrace();
            return HelpfulUtils.getResponseEntity(HelpfulUtils.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
