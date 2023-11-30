package com.example.cyberwalletapi.entities;

import com.example.cyberwalletapi.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String address;
    private Enum<Roles> role;
    private Double balance;

    @Column(name = "DateOfRegister")
    private Date dateOfRegister;
}

