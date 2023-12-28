package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.FindTransactionsDTO;
import com.example.cyberwalletapi.dto.TransactionRequest;
import com.example.cyberwalletapi.dto.TransactionResponseDTO;
import com.example.cyberwalletapi.dto.WithdrawAmountDTO;
import com.example.cyberwalletapi.services.UserTransactionService;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping(path = "/getRecentTransactions")
    public ResponseEntity<List<TransactionResponseDTO>> recentTransactions(@RequestBody FindTransactionsDTO findTransactionsDTO){
        try {
            return userTransactionService.getRecentOrders(findTransactionsDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
//        return HelpfulUtils.getResponseEntity(HelpfulUtils.UNAUTHORIZED_ACCESS,HttpStatus.FORBIDDEN);
        return null;
    }
    @PostMapping(path = "/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest transactionRequest, @RequestHeader("Authorization") String token) {
        try {
            return userTransactionService.userTransaction(transactionRequest,token);
        } catch (Exception e) {
            e.printStackTrace();
            return HelpfulUtils.getResponseEntity(HelpfulUtils.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(path = "/getAllRecentTransactions")
    public ResponseEntity<List<TransactionResponseDTO>> allRecentTransactions(@RequestBody FindTransactionsDTO findTransactionsDTO,@RequestHeader("Authorization") String token){
        try {
            return userTransactionService.getAllRecentOrders(findTransactionsDTO,token);
        }catch (Exception e){
            e.printStackTrace();
        }
//        return HelpfulUtils.getResponseEntity(HelpfulUtils.UNAUTHORIZED_ACCESS,HttpStatus.FORBIDDEN);
        return null;
    }
    @PostMapping(path = "/api/withdraw")
    public ResponseEntity<String>withdraw(@RequestBody WithdrawAmountDTO withdrawAmountDTO,@RequestHeader("Authorization") String token){
        try {
            userTransactionService.withdraw(token, withdrawAmountDTO.getAmount());
        }catch (Exception e){
            e.printStackTrace();
            return HelpfulUtils.getResponseEntity("An error has ocurred",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    return null;
    }

}
