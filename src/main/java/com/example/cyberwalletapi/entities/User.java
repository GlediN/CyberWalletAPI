package com.example.cyberwalletapi.entities;

import com.example.cyberwalletapi.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    @Id
    private String id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String address;
    private Enum<Roles> role;

    private Double balance;
    @Column(name = "DateOfRegister")
    private LocalDateTime dateOfRegister;
    public void setPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}

