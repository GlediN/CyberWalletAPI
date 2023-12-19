package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.FindTransactionsDTO;
import com.example.cyberwalletapi.dto.LoginRequest;
import com.example.cyberwalletapi.dto.SignUpRequest;
import com.example.cyberwalletapi.dto.TransactionResponseDTO;
import com.example.cyberwalletapi.services.UserService;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {

            userService.signUp(signUpRequest);
            return HelpfulUtils.getResponseEntity("Account created successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity(HelpfulUtils.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            return userService.login(loginRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity(HelpfulUtils.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping(path = "/checkToken")
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity(HelpfulUtils.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping(path = "/getRecentTransactions")
    public ResponseEntity<TransactionResponseDTO> recentTransactions(@RequestBody FindTransactionsDTO findTransactionsDTO){
        try {
            return userService.getRecentOrders(findTransactionsDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
//        return HelpfulUtils.getResponseEntity(HelpfulUtils.UNAUTHORIZED_ACCESS,HttpStatus.FORBIDDEN);
        return null;
    }
}
