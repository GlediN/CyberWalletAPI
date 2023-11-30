package com.example.cyberwalletapi.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@Table(name = "Transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "UserTransactionID", nullable = false)
    private UserTransaction userTransaction;
    private Double amount;
    @Column(name = "DateOfTransaction")
    private Date dateOfTransaction;
}

