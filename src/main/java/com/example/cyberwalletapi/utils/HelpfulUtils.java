package com.example.cyberwalletapi.utils;

import com.example.cyberwalletapi.dto.TransactionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class HelpfulUtils {

    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String INVALID_DATA = "Invalid Data";

    public static final String UNAUTHORIZED_ACCESS = "Unauthorized Access";

    private HelpfulUtils(){

    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
    public static ResponseEntity<List<TransactionResponseDTO>> getResponseEntity1(List<TransactionResponseDTO> responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<List<TransactionResponseDTO>>(responseMessage, httpStatus);
    }

}
