package com.example.cyberwalletapi.services;

import com.example.cyberwalletapi.dto.*;
import com.example.cyberwalletapi.dto.AccountChange.*;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import com.example.cyberwalletapi.enums.Roles;
import com.example.cyberwalletapi.jwt.CustomerUserDetailsService;
import com.example.cyberwalletapi.jwt.JwtUtil;
import com.example.cyberwalletapi.repositories.UserDAO;
import com.example.cyberwalletapi.utils.ApiResponse;
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
import java.util.*;
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
                User existingUser = userDao.findByEmailId(signUpRequest.getEmail());
                if (existingUser != null) {
                    return HelpfulUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                } else if (Objects.equals(existingUser.getEmail(), signUpRequest.getEmail())){
                    userDao.save(getUserFromSignUpRequest(signUpRequest));
                    return HelpfulUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                }else return HelpfulUtils.getResponseEntity(HelpfulUtils.INVALID_DATA,HttpStatus.BAD_REQUEST);
            } else {
                return HelpfulUtils.getResponseEntity(HelpfulUtils.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HelpfulUtils.getResponseEntity(HelpfulUtils.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    public boolean isUserAdmin(FindTransactionsDTO findTransactionsDTO) {
        String userRole = userDao.isAdmin(findTransactionsDTO.getEmail());
        return Objects.equals(userRole, "ADMIN");
    }
    //Method for showing user role on login
    public boolean isUserAdminString(String string) {
        String userRole = userDao.isAdmin(string);
        if (userRole == "ADMIN") {
            return true;
        } else return false;
    }
    //Method for showing user balance on login made by gledi
    public ResponseEntity<FindUsernameDTO> getUserName(String email) {
        try {
            FindUsernameDTO userName = new FindUsernameDTO();
            userName.setName(userDao.getUserName(email));
            return new ResponseEntity<>(userName, HttpStatus.OK);
        } catch (Exception e) {
//            return HelpfulUtils.getResponseEntity("Could not find Username",HttpStatus.BAD_REQUEST);
            return null;
        }
    }

        //Method for showing user balance on login made by gledi
    public ResponseEntity<FindBalanceResponse> getUserBalance(String email) {
        try {
            FindBalanceResponse userBalance = new FindBalanceResponse();
            Double balance = userDao.selectUserBalance(email);
            userBalance.setBalance(balance);
            return new ResponseEntity<>(userBalance, HttpStatus.OK);
        } catch (Exception e) {
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

        //Below here are the admin methods where you can change the user details
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> updateUserEmail(String authHeader,EmailChangeRequestDTO emailChangeRequestDTO) {
        try {
            // Extract token from Authorization header
            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            boolean isUserAdmin = isUserAdminString(claims.getSubject());
            if (isUserAdmin) {
                if (areEmailsValid(emailChangeRequestDTO.getOldEmail(),emailChangeRequestDTO.getNewEmail())) {
                    userDao.updateUserEmail(emailChangeRequestDTO.getNewEmail(),emailChangeRequestDTO.getOldEmail());
                    AccountChangeResponseDTO accountChangeResponseDTO=new AccountChangeResponseDTO();
                    accountChangeResponseDTO.setMessage(emailChangeRequestDTO.getNewEmail());
                    return ResponseEntity.ok(ApiResponse.success(accountChangeResponseDTO));
                } else ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
            } else HelpfulUtils.getResponseEntity("User is not a admin", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal Server Error"));
    }

    private boolean areEmailsValid(String oldEmail, String newEmail) {
        User user=userDao.findByEmailId(oldEmail);
        if (oldEmail!=null){
            if (user.getEmail()!=null){
                return true;
            }
        }
        return false;
    }

    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> updateUserBalance(String authHeader, BalanceChangeRequestDTO balanceChangeRequestDTO) {
        try {
            // Extract token from Authorization header
            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            boolean isUserAdmin = isUserAdminString(claims.getSubject());
            if (isUserAdmin) {
                if (balanceChangeRequestDTO.getBalance() != null) {
                    userDao.updateUserBalance(balanceChangeRequestDTO.getBalance(), balanceChangeRequestDTO.getEmail());
                    AccountChangeResponseDTO accountChangeResponseDTO=new AccountChangeResponseDTO();
                    accountChangeResponseDTO.setMessage(balanceChangeRequestDTO.getEmail());
                    return ResponseEntity.ok(ApiResponse.success(accountChangeResponseDTO));
                } else ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
            } else HelpfulUtils.getResponseEntity("User is not a admin", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal Server Error"));
    }
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> updateUserName(String authHeader, NameChangeRequestDTO nameChangeRequestDTO) {
        try {
            // Extract token from Authorization header
            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            boolean isUserAdmin = isUserAdminString(claims.getSubject());
            if (isUserAdmin) {
                if (nameChangeRequestDTO.getName() != null) {
                    userDao.updateUserName(nameChangeRequestDTO.getName(), nameChangeRequestDTO.getEmail());
                    AccountChangeResponseDTO accountChangeResponseDTO=new AccountChangeResponseDTO();
                    accountChangeResponseDTO.setMessage(nameChangeRequestDTO.getName());
                    return ResponseEntity.ok(ApiResponse.success(accountChangeResponseDTO));
                }
            } else return ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal Server Error"));
    }
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> updateUserPassword(String authHeader, PasswordChangeRequestDTO passwordChangeRequestDTO) {
        try {
            // Extract token from Authorization header
            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            boolean isUserAdmin = isUserAdminString(claims.getSubject());
            if (isUserAdmin) {
                if (passwordChangeRequestDTO.getEmail() != null) {
                    String newPassword = passwordChangeRequestDTO.getPassword();
                    User user=userDao.findByEmailId(passwordChangeRequestDTO.getEmail());
                    user.setPassword(newPassword);
                    userDao.updateUserPassword(user.getPassword(), passwordChangeRequestDTO.getEmail());
                    AccountChangeResponseDTO accountChangeResponseDTO=new AccountChangeResponseDTO();
                    accountChangeResponseDTO.setMessage(passwordChangeRequestDTO.getEmail());
                    return ResponseEntity.ok(ApiResponse.success(accountChangeResponseDTO));
                }
            } else return ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal Server Error"));
    }

    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> updateUserAddress(String authHeader, AddressChangeRequestDTO addressChangeRequestDTO) {
        try {
            // Extract token from Authorization header
            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            boolean isUserAdmin = isUserAdminString(claims.getSubject());
            if (isUserAdmin) {
                if (addressChangeRequestDTO.getEmail() != null) {
                    if (addressChangeRequestDTO.getAddress()!=""){
                        userDao.updateUserAddress(addressChangeRequestDTO.getAddress(),addressChangeRequestDTO.getEmail());
                        AccountChangeResponseDTO accountChangeResponseDTO=new AccountChangeResponseDTO();
                        accountChangeResponseDTO.setMessage(addressChangeRequestDTO.getAddress());
                        return ResponseEntity.ok(ApiResponse.success(accountChangeResponseDTO));
                }else return ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
                }
            } else return ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal Server Error"));
    }
    public ResponseEntity<ApiResponse<AccountChangeResponseDTO>> updateUserRole(String authHeader, RoleChangeRequestDTO roleChangeRequestDTO){
        try {

            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            boolean isUserAdmin = isUserAdminString(claims.getSubject());
            if (isUserAdmin) {
                if (roleChangeRequestDTO.getEmail() != null) {
                    if (roleChangeRequestDTO.getRole()!=null){
                        if (isRoleValid(String.valueOf(roleChangeRequestDTO.getRole()))){
                        userDao.updateUserRole(roleChangeRequestDTO.getRole(),roleChangeRequestDTO.getEmail());
                        AccountChangeResponseDTO accountChangeResponseDTO=new AccountChangeResponseDTO();
                        accountChangeResponseDTO.setMessage(roleChangeRequestDTO.getEmail());
                        return ResponseEntity.ok(ApiResponse.success(accountChangeResponseDTO));
                    }else return ResponseEntity.badRequest().body(ApiResponse.error("Role doesnt exist"));
                    }else return ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
                }
            } else return ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal Server Error"));
    }

    private boolean isRoleValid(String role){
        if (role=="ADMIN"||role=="USER"){
            return true;
        }else return false;
    }

    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(String authHeader){
        try {

            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            boolean isUserAdmin = isUserAdminString(claims.getSubject());
            if (isUserAdmin) {
                List<User>userResponseList=new ArrayList<>();
                List<User>users=userDao.getAllUsers();
                if (users != null && !users.isEmpty()) {
                    for (User user : users) {
                        User userResponseDTO = new User();
                        userResponseDTO.setId(user.getId());
                        userResponseDTO.setName(user.getName());
                        userResponseDTO.setRole(user.getRole());
                        userResponseDTO.setAddress(user.getAddress());
                        userResponseDTO.setBalance(user.getBalance());
                        userResponseDTO.setEmail(user.getEmail());
                        userResponseDTO.setDateOfRegister(user.getDateOfRegister());
                        userResponseList.add(userResponseDTO);
                    }
                    return ResponseEntity.ok(ApiResponse.success(userResponseList));
                }return ResponseEntity.ok().body(ApiResponse.error("There arent any users"));
            } else return ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal Server Error"));
    }
}
