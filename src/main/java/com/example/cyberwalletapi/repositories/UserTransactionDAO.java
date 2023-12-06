package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTransactionDAO extends JpaRepository<UserTransaction,Integer> {
    @Query("select u from User u where u.email=:email")
    User findByEmailId(@Param("email") String email);
}
