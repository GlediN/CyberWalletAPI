package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.entities.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTransactionDAO extends JpaRepository<UserTransaction,Integer> {
}
