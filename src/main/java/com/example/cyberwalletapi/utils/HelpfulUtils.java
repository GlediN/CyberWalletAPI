package com.example.cyberwalletapi.utils;

import com.example.cyberwalletapi.dto.TransactionResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelpfulUtils {

    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String INVALID_DATA = "Invalid Data";

    public static final String UNAUTHORIZED_ACCESS = "Unauthorized Access";

    private HelpfulUtils(){

    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
    public static ResponseEntity<TransactionResponseDTO> getResponseEntity1(TransactionResponseDTO responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<TransactionResponseDTO>(responseMessage, httpStatus);
    }

}
