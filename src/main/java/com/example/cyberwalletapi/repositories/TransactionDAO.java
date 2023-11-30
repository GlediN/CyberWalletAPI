package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDAO extends JpaRepository<Transactions,Integer> {
}
