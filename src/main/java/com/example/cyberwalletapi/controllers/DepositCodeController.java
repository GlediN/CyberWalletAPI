package com.example.cyberwalletapi.controllers;

import com.example.cyberwalletapi.dto.GiftCardResponseDTO;
import com.example.cyberwalletapi.repositories.DepositDAO;
import com.example.cyberwalletapi.services.DepositCodesService;
import com.example.cyberwalletapi.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class DepositCodeController {
    private final DepositDAO depositDAO;
    private final DepositCodesService depositCodesService;
    @PostMapping(path = "/getCode")
    public ResponseEntity<GiftCardResponseDTO>getGiftCard(@RequestHeader("Authorization") String token){
        try {
            return depositCodesService.getGiftCardCode(token);
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;
    }
    @PostMapping("/addBalanceByCode")
    public ResponseEntity<ApiResponse<GiftCardResponseDTO>> addBalanceByCode
    (@RequestBody GiftCardResponseDTO giftCardResponseDTO,@RequestHeader ("Authorization") String token){
        try {
            return depositCodesService.useGiftCardCode(giftCardResponseDTO,token);
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;}
}
