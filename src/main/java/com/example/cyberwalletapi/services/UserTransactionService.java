package com.example.cyberwalletapi.services;

import com.example.cyberwalletapi.dto.TransactionRequest;
import com.example.cyberwalletapi.dto.UserDataDTO;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import com.example.cyberwalletapi.repositories.UserDAO;
import com.example.cyberwalletapi.repositories.UserTransactionDAO;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class UserTransactionService {
    private final UserTransactionDAO userTransactionDAO;
    private final UserDAO userDAO;
    private Jwt authenticationToken;
    private final String secret = System.getenv("SECRET_KEY_FINALPROJECT");
    private static final Logger logger = LoggerFactory.getLogger(UserTransactionService.class);
    public ResponseEntity<String> userTransaction(TransactionRequest transactionRequest, String authorizationHeader) {
        try {
            // Extract token from Authorization header
            String token = extractToken(authorizationHeader);

            // Validate and parse the token
            Claims claims = validateAndParseToken(token);

            // Check if the subject claim matches the email in the TransactionRequest
            if (claims != null && claims.getSubject().equals(transactionRequest.getEmail())) {
                UserTransaction userTransaction = userTransactionDAO.findByEmailId(transactionRequest.getEmail());
                userTransactionDAO.save(getTransactionFromTransactionRequest(transactionRequest));
                return HelpfulUtils.getResponseEntity("Transaction completed successfully", HttpStatus.OK);
            } else {
                return HelpfulUtils.getResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            logger.error("Error processing transaction", e);
            return HelpfulUtils.getResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractToken(String authorizationHeader) {
        // Extract Bearer token from Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private Claims validateAndParseToken(String token) {
        // Validate and parse the JWT token
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity<UserDataDTO> getUserFromEmail(String email){
        try{
            User user = (userDAO.findByEmailId(email));
            UserDataDTO userDataDTO = new UserDataDTO();
            userDataDTO.setEmail(user.getEmail());
            userDataDTO.setAddress(user.getAddress());
            userDataDTO.setName(user.getName());
            userDataDTO.setBalance(user.getBalance());
            return new ResponseEntity<>(userDataDTO,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private UserTransaction getTransactionFromTransactionRequest(TransactionRequest transactionRequest) {
        UserTransaction userTransaction =new UserTransaction();
        userTransaction.setId(String.valueOf(UUID.randomUUID()));
        userTransaction.setDateOfTransaction(LocalDateTime.now());
        userTransaction.setAmount(transactionRequest.getAmount());
        userTransaction.setRecipient(transactionRequest.getRecipient());
        userTransaction.setDescription(transactionRequest.getDescription());
        User user = userDAO.findByEmailId(transactionRequest.getEmail());

        if (user != null) {
            userTransaction.setUserID(user);
            System.out.println(userTransaction.getUserID());
        } else {
            throw new RuntimeException("Something wrong with getTransactionFromTransactionRequest method");
        }
        return userTransaction;
    }}


