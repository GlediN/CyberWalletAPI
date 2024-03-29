package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.*;
import com.example.cyberwalletapi.services.UserService;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {

            userService.signUp(signUpRequest);
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

    @PostMapping(path = "/getUsername")
    public ResponseEntity<FindUsernameDTO>getUserName(@RequestBody FindTransactionsDTO findTransactionsDTO){
        try {
            return userService.getUserName(findTransactionsDTO.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }
//    return HelpfulUtils.getResponseEntity("Not found",HttpStatus.BAD_REQUEST);
        return null;
    }
    @PostMapping(path = "/getBalance")
    public ResponseEntity<FindBalanceResponse>getBalance(@RequestBody FindTransactionsDTO findTransactionsDTO){
        try {
            return userService.getUserBalance(findTransactionsDTO.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }
//    return HelpfulUtils.getResponseEntity("Not found",HttpStatus.BAD_REQUEST);
        return null;
    }
    @PostMapping(path = "/isAdmin")
    public boolean isAdmin(@RequestBody FindTransactionsDTO findTransactionsDTO){
        return userService.isUserAdmin(findTransactionsDTO);
    }
}
