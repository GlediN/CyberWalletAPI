package com.example.cyberwalletapi.services;

import com.example.cyberwalletapi.dto.SignUpRequest;
import com.example.cyberwalletapi.dto.TransactionRequest;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import com.example.cyberwalletapi.enums.Roles;
import com.example.cyberwalletapi.repositories.UserTransactionDAO;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class UserTransactionService {
    private final UserTransactionDAO userTransactionDAO;
    public ResponseEntity<String> userTransaction(TransactionRequest transactionRequest) {
        try {
            if (validateTransactionRequest(transactionRequest)) {
                UserTransaction userTransaction = userTransactionDAO.findByEmailId(transactionRequest.getEmail());
                if (Objects.isNull(userTransaction)) {
                    userTransactionDAO.save(getTransactionFromTransactionRequest(transactionRequest));
                    return HelpfulUtils.getResponseEntity("Transaction Completed", HttpStatus.OK);
                } else {
                    return HelpfulUtils.getResponseEntity("Transaction Failed", HttpStatus.BAD_REQUEST);
                }
            } else {
                return HelpfulUtils.getResponseEntity(HelpfulUtils.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity(HelpfulUtils.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateTransactionRequest(TransactionRequest transactionRequest) {
        return transactionRequest.getEmail() != null && transactionRequest.getAmount()!= null&&transactionRequest.getRecipient()!=null;
    }
//    private boolean balanceIsEnough(TransactionRequest transactionRequest){
//        User user;
//        userTransactionDAO.g
//        if (transactionRequest.getUserBalance())
//    }
    private UserTransaction getTransactionFromTransactionRequest(TransactionRequest transactionRequest) {
        User user=new User();
        UserTransaction userTransaction =new UserTransaction();
        userTransaction.setId(String.valueOf(UUID.randomUUID()));
        userTransaction.setDateOfTransaction(LocalDateTime.now());
        userTransaction.setAmount(transactionRequest.getAmount());
        userTransaction.setUser(user);
        return userTransaction;
    }}


