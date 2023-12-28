package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.AccountChange.*;
import com.example.cyberwalletapi.dto.FindTransactionsDTO;
import com.example.cyberwalletapi.services.UserService;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping(path = "/admin/changeUsername")
    public ResponseEntity<String>changeUsername(@RequestBody NameChangeRequestDTO nameChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserName(token, nameChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
    return HelpfulUtils.getResponseEntity("Error",HttpStatus.BAD_REQUEST);
    }
    @PostMapping(path = "/admin/changeEmail")
    public ResponseEntity<String>changeEmail(@RequestBody EmailChangeRequestDTO nameChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserEmail(token, nameChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity("Error",HttpStatus.BAD_REQUEST);
    }
    @PostMapping(path = "/admin/changeBalance")
    public ResponseEntity<String>changeBalance(@RequestBody BalanceChangeRequestDTO balanceChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserBalance(token, balanceChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity("Error",HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/admin/changePassword")
    public ResponseEntity<String>changePassword(@RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserPassword(token, passwordChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(path = "/admin/changeAddress")
    public ResponseEntity<String>changeAddress(@RequestBody AddressChangeRequestDTO addressChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserAddress(token, addressChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping(path = "/admin/changeRole")
    public ResponseEntity<String>changeRole(@RequestBody RoleChangeRequestDTO roleChangeRequestDTO, @RequestHeader("Authorization")String token){
        try {
            return userService.updateUserRole(token, roleChangeRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
