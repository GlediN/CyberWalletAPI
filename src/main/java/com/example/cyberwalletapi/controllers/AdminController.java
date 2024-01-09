package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.AccountChange.*;
import com.example.cyberwalletapi.dto.TransactionDateDTO;
import com.example.cyberwalletapi.dto.TransactionResponseDTO;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import com.example.cyberwalletapi.services.UserService;
import com.example.cyberwalletapi.services.UserTransactionService;
import com.example.cyberwalletapi.utils.ApiResponse;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final UserTransactionService userTransactionService;

    //This is the controller where the admin can change all the user details

    @PostMapping(path = "/admin/changeUsername")
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> changeUsername(@RequestBody NameChangeRequestDTO nameChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserName(token, nameChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;
    }
    @PostMapping(path = "/admin/changeEmail")
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> changeEmail(@RequestBody EmailChangeRequestDTO nameChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserEmail(token, nameChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping(path = "/admin/changeBalance")
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> changeBalance(@RequestBody BalanceChangeRequestDTO balanceChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserBalance(token, balanceChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(path = "/admin/changePassword")
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>>changePassword(@RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserPassword(token, passwordChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(path = "/admin/changeAddress")
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>>changeAddress(@RequestBody AddressChangeRequestDTO addressChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserAddress(token, addressChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping(path = "/admin/changeRole")
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>>changeRole(@RequestBody RoleChangeRequestDTO roleChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserRole(token, roleChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping(path = "/admin/getAllUsers")
    public ResponseEntity<ApiResponse<List<User>>>getAllUsers(@RequestHeader("Authorization")String token){
        try {
            return userService.getAllUsers(token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping(path = "/admin/getTransactionsByDate")
    public ResponseEntity<List<TransactionResponseDTO>>getTransactionsByDate(@RequestHeader("Authorization")String token, @RequestBody TransactionDateDTO transactionDateDTO){
        try {
            return userTransactionService.getOrdersByDate(transactionDateDTO,token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
