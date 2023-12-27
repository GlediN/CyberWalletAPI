package com.example.cyberwalletapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DepositCodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int amount;
    private String code;
}
