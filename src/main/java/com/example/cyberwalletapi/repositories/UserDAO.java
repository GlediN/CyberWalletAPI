package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDAO extends JpaRepository<User,Integer> {
    @Query("SELECT u.balance FROM User u WHERE u.email = :email")
    Double selectUserBalance( @Param("email") String email);
    @Query("select u from User u where u.email=:email")
    User findByEmailId(@Param("email") String email);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.balance = u.balance-:amount WHERE u.email = :email")
    void updateBalanceByEmail(@Param("amount") Double amount,@Param("email")String email);
}
