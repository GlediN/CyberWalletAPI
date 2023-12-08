package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.TransactionRequest;
import com.example.cyberwalletapi.services.UserTransactionService;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserTransactionController {
    private final UserTransactionService userTransactionService;


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
