package com.example.cyberwalletapi.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "UserTransaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    private Double amount;

    @Column(name = "DateOfTransaction")
    private Date dateOfTransaction;
    private String description;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;
}



