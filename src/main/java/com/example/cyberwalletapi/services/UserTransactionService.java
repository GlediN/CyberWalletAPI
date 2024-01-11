package com.example.cyberwalletapi.services;

import com.example.cyberwalletapi.dto.*;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import com.example.cyberwalletapi.repositories.UserDAO;
import com.example.cyberwalletapi.repositories.UserTransactionDAO;
import com.example.cyberwalletapi.utils.ApiResponse;
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
import java.util.ArrayList;
import java.util.List;
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
                User userTransaction = userDAO.findByEmailId(transactionRequest.getEmail());
                if (userTransaction.getBalance()>= transactionRequest.getAmount()){
                if (selectBalance(transactionRequest)) {
                    if (updateRecipientBalance(transactionRequest)) {
                        userDAO.updateSenderBalanceByEmail(transactionRequest.getAmount(), transactionRequest.getRecipient());
                    }
                    userTransactionDAO.save(getTransactionFromTransactionRequest(transactionRequest));
                    updateBalance(transactionRequest);
                }
                return HelpfulUtils.getResponseEntity("Transaction Saved", HttpStatus.OK);
            }else return HelpfulUtils.getResponseEntity("Not enough balance",HttpStatus.BAD_REQUEST);
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

    public ResponseEntity<UserDataDTO> getUserFromEmail(String email) {
        try {
            User user = (userDAO.findByEmailId(email));
            UserDataDTO userDataDTO = new UserDataDTO();
            userDataDTO.setEmail(user.getEmail());
            userDataDTO.setAddress(user.getAddress());
            userDataDTO.setName(user.getName());
            userDataDTO.setBalance(user.getBalance());
            return new ResponseEntity<>(userDataDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private UserTransaction getTransactionFromTransactionRequest(TransactionRequest transactionRequest) {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setId(String.valueOf(UUID.randomUUID()));
        userTransaction.setDateOfTransaction(LocalDateTime.now());
        userTransaction.setAmount(transactionRequest.getAmount());
        userTransaction.setRecipient(transactionRequest.getRecipient());
        userTransaction.setDescription(transactionRequest.getDescription());
        User user = userDAO.findByEmailId(transactionRequest.getEmail());

        if (user != null) {
            userTransaction.setUserID(user);
        } else {
            throw new RuntimeException("Something wrong with getTransactionFromTransactionRequest method");
        }
        return userTransaction;
    }

    private boolean selectBalance(TransactionRequest transactionRequest) {
        if (transactionRequest != null) {
            Double user = userDAO.selectUserBalance(transactionRequest.getEmail());
            return user >= transactionRequest.getAmount();
        }
        return false;
    }

    private boolean updateRecipientBalance(TransactionRequest transactionRequest) {
        try {
            User doesRecipentExist = userTransactionDAO.findByEmailId(transactionRequest.getRecipient());
            if (doesRecipentExist.getEmail() != null) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private void updateBalance(TransactionRequest transactionRequest) {

        userDAO.updateBalanceByEmail(transactionRequest.getAmount(), transactionRequest.getEmail());

    }

    public ResponseEntity<List<TransactionResponseDTO>> getRecentOrders(FindTransactionsDTO findTransactionsDTO) {
        List<TransactionResponseDTO> transactionResponseDTOList = new ArrayList<>();
        try {
            User user = userDAO.findByEmailId(findTransactionsDTO.getEmail());
            List<UserTransaction> userTransactions = userDAO.getLatestTransactions(user.getId());

            if (userTransactions != null && !userTransactions.isEmpty()) {
                for (UserTransaction userTransaction : userTransactions) {
                    TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
                    transactionResponseDTO.setDescription(userTransaction.getDescription());
                    transactionResponseDTO.setAmount(String.valueOf(userTransaction.getAmount()));
                    transactionResponseDTO.setRecipient(userTransaction.getRecipient());
                    transactionResponseDTO.setDateOfTransaction(userTransaction.getDateOfTransaction());
                    transactionResponseDTO.setId(userTransaction.getId());

                    transactionResponseDTOList.add(transactionResponseDTO);
                }

                return new ResponseEntity<>(transactionResponseDTOList, HttpStatus.OK);
            } else {
                return HelpfulUtils.getResponseEntity1(transactionResponseDTOList, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity1(transactionResponseDTOList, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<TransactionResponseDTO>> getAllRecentOrders(FindTransactionsDTO findTransactionsDTO, String authorizationHeader) {
        List<TransactionResponseDTO> transactionResponseDTOList = new ArrayList<>();
        try {
            List<UserTransaction> userTransactions = userDAO.getAllTransactions(findTransactionsDTO.getEmail());
            String userRole = userDAO.isAdmin(findTransactionsDTO.getEmail());
            String token = extractToken(authorizationHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            // Check if the subject claim matches the email in the TransactionRequest
            if (claims != null && claims.getSubject().equals(findTransactionsDTO.getEmail())) {
                if (userRole == "ADMIN") {
                    if (userTransactions != null && !userTransactions.isEmpty()) {
                        for (UserTransaction userTransaction : userTransactions) {
                            TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
                            transactionResponseDTO.setDescription(userTransaction.getDescription());
                            transactionResponseDTO.setAmount(String.valueOf(userTransaction.getAmount()));
                            transactionResponseDTO.setRecipient(userTransaction.getRecipient());
                            transactionResponseDTO.setDateOfTransaction(userTransaction.getDateOfTransaction());
                            transactionResponseDTO.setId(userTransaction.getId());

                            transactionResponseDTOList.add(transactionResponseDTO);
                        }

                        return new ResponseEntity<>(transactionResponseDTOList, HttpStatus.OK);
                    } else {
                        return HelpfulUtils.getResponseEntity1(transactionResponseDTOList, HttpStatus.BAD_REQUEST);
                    }
                }
                return HelpfulUtils.getResponseEntity1(transactionResponseDTOList, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity1(transactionResponseDTOList, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<List<TransactionResponseDTO>> getOrdersByDate(TransactionDateDTO transactionDateDTO, String authorizationHeader) {
        List<TransactionResponseDTO> transactionResponseDTOList = new ArrayList<>();
        try {
            LocalDateTime date = LocalDateTime.parse(transactionDateDTO.getDate() + "T00:00:00");
            LocalDateTime endDate = date.plusDays(1);
            List<UserTransaction> userTransactions = userTransactionDAO.getTransactionsByDate(date, endDate);
            String token = extractToken(authorizationHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            // Check if the subject claim matches the email in the TransactionRequest
            if (claims != null) {
                if (userTransactions != null && !userTransactions.isEmpty()) {
                    for (UserTransaction userTransaction : userTransactions) {
                        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
                        transactionResponseDTO.setDescription(userTransaction.getDescription());
                        transactionResponseDTO.setAmount(String.valueOf(userTransaction.getAmount()));
                        transactionResponseDTO.setRecipient(userTransaction.getRecipient());
                        transactionResponseDTO.setDateOfTransaction(userTransaction.getDateOfTransaction());
                        transactionResponseDTO.setId(userTransaction.getId());

                        transactionResponseDTOList.add(transactionResponseDTO);
                    }

                    return new ResponseEntity<>(transactionResponseDTOList, HttpStatus.OK);
                } else {
                    return HelpfulUtils.getResponseEntity1(transactionResponseDTOList, HttpStatus.BAD_REQUEST);
                }
            }
            return HelpfulUtils.getResponseEntity1(transactionResponseDTOList, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity1(transactionResponseDTOList, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> withdraw(String authHeader, Double amount) {
        try {
            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            if (claims != null) {
                User user = userDAO.findByEmailId(claims.getSubject());
                Double userBalance = userDAO.selectUserBalance(user.getEmail());
                if (userBalance>=amount) {
                    Double updatedBalance = userBalance - amount;
                    userTransactionDAO.withdrawFromUser(updatedBalance, user.getEmail());
                    return HelpfulUtils.getResponseEntity("Funds withdrew", HttpStatus.OK);
                } else {
                    return HelpfulUtils.getResponseEntity("Withdraw amount  is more than avalible balance", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity("An error has occured", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


