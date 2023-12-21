package com.example.cyberwalletapi.services;

import com.example.cyberwalletapi.dto.*;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import com.example.cyberwalletapi.enums.Roles;
import com.example.cyberwalletapi.jwt.CustomerUserDetailsService;
import com.example.cyberwalletapi.jwt.JwtUtil;
import com.example.cyberwalletapi.repositories.UserDAO;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDao;
    private final CustomerUserDetailsService customerUsersDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder bcryptPasswordEncoder;
    private final String secret = System.getenv("SECRET_KEY_FINALPROJECT");
    private static final Logger logger = Logger.getLogger(UserService.class.getName());



    public ResponseEntity<String> signUp(SignUpRequest signUpRequest) {
        try {
            if (validateSignUpRequest(signUpRequest)) {
                User user = userDao.findByEmailId(signUpRequest.getEmail());
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromSignUpRequest(signUpRequest));
                    return HelpfulUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                } else {
                    return HelpfulUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return HelpfulUtils.getResponseEntity(HelpfulUtils.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity(HelpfulUtils.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                String jsonToken = "{\"token\":\"";
                String jsonEmail = "\",\"email\":\"";
                String token = jwtUtil.generateToken(customerUsersDetailsService
                        .getUserDetail()
                        .getEmail(), (Roles) customerUsersDetailsService.getUserDetail().getRole());
                String email = customerUsersDetailsService.getUserDetail().getEmail();

                return new ResponseEntity<>(jsonToken + token + jsonEmail + email + "\"}", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("{\"message\":\"Bad Credentials.\"}", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<UserDataDTO> getUserFromEmail(String email) {
        try {
            User user = (userDao.findByEmailId(email));
            UserDataDTO userDataDTO = new UserDataDTO();
            userDataDTO.setEmail(user.getEmail());
            userDataDTO.setAddress(user.getAddress());
            userDataDTO.setName(user.getName());
            return new ResponseEntity<>(userDataDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> checkToken() {
        try {
            return HelpfulUtils.getResponseEntity("true", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HelpfulUtils.getResponseEntity(HelpfulUtils.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpRequest(SignUpRequest signUpRequest) {
        return signUpRequest.getEmail() != null && signUpRequest.getPassword() != null;
    }

    private User getUserFromSignUpRequest(SignUpRequest signUpRequest) {
        User user = new User();
        user.setId(String.valueOf(UUID.randomUUID()));
        user.setDateOfRegister(LocalDateTime.now());
        user.setName(signUpRequest.getName());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setAddress(signUpRequest.getAddress());
        user.setRole(Roles.USER);
        user.setBalance((double) 0);
        return user;
    }

    public ResponseEntity<List<TransactionResponseDTO>> getRecentOrders(FindTransactionsDTO findTransactionsDTO) {
        List<TransactionResponseDTO> transactionResponseDTOList = new ArrayList<>();
        try {
            User user = userDao.findByEmailId(findTransactionsDTO.getEmail());
            List<UserTransaction> userTransactions = userDao.getLatestTransactions(user.getId());

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
    public ResponseEntity<FindUsernameDTO> getUserName(String email){
        try {
            FindUsernameDTO userName=new FindUsernameDTO();
                   userName.setName(userDao.getUserName(email));
           return new ResponseEntity<>(userName,HttpStatus.OK) ;
        }catch (Exception e){
//            return HelpfulUtils.getResponseEntity("Could not find Username",HttpStatus.BAD_REQUEST);
            return null;
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

}
