package com.example.cyberwalletapi.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserTransaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String recipient;
    private Double amount;
    private LocalDateTime dateOfTransaction;
    private String description;
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User userID;

}



