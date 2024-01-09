package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserTransactionDAO extends JpaRepository<UserTransaction,Integer> {
    @Query("select u from User u where u.email=:email")
    User findByEmailId(@Param("email") String email);
    @Modifying
    @Transactional
    @Query("update User u set u.balance=:amount where u.email=:email")
    void withdrawFromUser(@Param("amount")Double amount,@Param("email")String email);
    @Query("SELECT ut FROM UserTransaction ut WHERE ut.dateOfTransaction BETWEEN :startDate AND :endDate")
    List<UserTransaction> getTransactionsByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


}
