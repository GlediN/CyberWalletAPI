package com.example.cyberwalletapi.services;

import com.example.cyberwalletapi.dto.GiftCardResponseDTO;
import com.example.cyberwalletapi.entities.DepositCodes;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.repositories.DepositDAO;
import com.example.cyberwalletapi.repositories.UserDAO;
import com.example.cyberwalletapi.repositories.UserTransactionDAO;
import com.example.cyberwalletapi.utils.ApiResponse;
import com.example.cyberwalletapi.utils.HelpfulUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositCodesService {
    private final DepositDAO depositDAO;
    private final UserDAO userDAO;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final String secret = System.getenv("SECRET_KEY_FINALPROJECT");
    private static final int CODE_LENGTH = 8;

    public String generateGiftCardCode() {
        StringBuilder code = new StringBuilder();

        SecureRandom random = new SecureRandom();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }

    @Transactional
    public ResponseEntity<GiftCardResponseDTO> getGiftCardCode(String authorizationHeader) {
        try {

            String token = extractToken(authorizationHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);
            // Check if the subject claim matches the email in the TransactionRequest

            String userRole = userDAO.isAdmin(claims.getSubject());
            if (claims != null) {
                if (userRole == "ADMIN") {
                    GiftCardResponseDTO giftCardResponseDTO = new GiftCardResponseDTO();
                    String giftCardCode = generateGiftCardCode();
                    DepositCodes depositCodes = new DepositCodes();
                    depositCodes.setCode(giftCardCode);
                    depositCodes.setAmount(25.0);
                    depositDAO.save(depositCodes);
                    giftCardResponseDTO.setAmount(depositCodes.getAmount());
                    giftCardResponseDTO.setCode(depositCodes.getCode());
                    return new ResponseEntity<>(giftCardResponseDTO, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<ApiResponse<GiftCardResponseDTO>>
    useGiftCardCode(GiftCardResponseDTO giftCardResponseDTO, String authHeader) {
        try {
            String token = extractToken(authHeader);
            // Validate and parse the token
            Claims claims = validateAndParseToken(token);

            DepositCodes depositCodes = depositDAO.getCode(giftCardResponseDTO.getCode());
            if (depositCodes != null) {
                if (claims != null) {
                    if (giftCardResponseDTO.getAmount() == depositCodes.getAmount()) {
                        User user = userDAO.findByEmailId(claims.getSubject());
                        user.setBalance(Double.valueOf(depositDAO.updateBalanceByCode(depositCodes.getAmount())));
                        // Your success response, assuming GiftCardResponseDTO is populated appropriately
                        return ResponseEntity.ok(ApiResponse.success(giftCardResponseDTO));
                    } else {
                        // Amount mismatch
                        return ResponseEntity.badRequest().body(ApiResponse.error("Amount mismatch"));
                    }
                } else {
                    // Claims validation failed
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Invalid token"));
                }
            } else {
                // Deposit code not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Deposit code not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Internal Server Error"));
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
