package com.example.cyberwalletapi.services;
import com.example.cyberwalletapi.dto.TransactionRequest;
import com.example.cyberwalletapi.dto.UserDataDTO;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import com.example.cyberwalletapi.repositories.UserDAO;
import com.example.cyberwalletapi.repositories.UserTransactionDAO;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor

public class UserTransactionService {
    private final UserTransactionDAO userTransactionDAO;
    private final UserDAO userDAO;
    private static final Logger logger = LoggerFactory.getLogger(UserTransactionService.class);


        public ResponseEntity<String> userTransaction(TransactionRequest transactionRequest) {
            try {
                UserTransaction userTransaction = userTransactionDAO.findByEmailId(transactionRequest.getEmail());
                        userTransactionDAO.save(getTransactionFromTransactionRequest(transactionRequest));
                        return HelpfulUtils.getResponseEntity("Transaction completed successfully", HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error processing transaction", e);
                return HelpfulUtils.getResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    //    public ResponseEntity<String> userTransaction(TransactionRequest transactionRequest) {
//        try {
//
//                UserTransaction userTransaction = userTransactionDAO.findByEmailId(transactionRequest.getEmail());
//
//                if (Objects.isNull(userTransaction)) {
//                        userTransactionDAO.save(getTransactionFromTransactionRequest(transactionRequest));
//                        return HelpfulUtils.getResponseEntity("Transactions Completed", HttpStatus.OK);
//                } else {
//                    System.out.println(userTransaction.toString());
//                    return HelpfulUtils.getResponseEntity("Transactions Failed", HttpStatus.BAD_REQUEST);
//                }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return HelpfulUtils.getResponseEntity(HelpfulUtils.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    private boolean balanceIsEnough(TransactionRequest transactionRequest){
//        return userDAO.updateUserBalance(transactionRequest.getEmail());
//    }
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


