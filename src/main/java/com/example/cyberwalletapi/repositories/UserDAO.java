package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDAO extends JpaRepository<User,Integer> {
    @Query("update User u set u.balance=u.balance+:amount where u.id=:id")
    void updateUserBalance(@Param("amount") Integer amount, @Param("id") Integer id);
    @Query("select u from User u where u.email=:email")
    User findByEmailId(@Param("email") String email);
}
