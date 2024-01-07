package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.dto.FindBalanceResponse;
import com.example.cyberwalletapi.dto.FindUsernameDTO;
import com.example.cyberwalletapi.entities.User;
import com.example.cyberwalletapi.entities.UserTransaction;
import com.example.cyberwalletapi.enums.Roles;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {
    @Query("SELECT u.balance FROM User u WHERE u.email = :email")
    Double selectUserBalance( @Param("email") String email);
    @Query("select u from User u where u.email=:email")
    User findByEmailId(@Param("email") String email);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.balance = u.balance-:amount WHERE u.email = :email")
    void updateBalanceByEmail(@Param("amount") Double amount,@Param("email")String email);
    @Query("select ut from UserTransaction ut where ut.userID.id=:userID")
    List<UserTransaction> getLatestTransactions(@Param("userID") String email);
    @Query("select  ut from UserTransaction ut ")
    List<UserTransaction> getAllTransactions(@Param("userID") String email);
    @Query("select u.name from User u where u.email =:email")
    String getUserName(@Param("email")String email);
    @Query("select u.role from User u where u.email=:email")
    String isAdmin(@Param("email")String email);
    @Modifying
    @Transactional
    @Query("update User u set u.email=:newEmail where u.email=:oldEmail")
    void updateUserEmail(@Param("newEmail")String newEmail,@Param("oldEmail")String oldEmail);
    @Modifying
    @Transactional
    @Query("update User u set u.balance=:newBalance where u.email=:email")
    void updateUserBalance(@Param("newBalance")Double balance,@Param("email")String email);
    @Modifying
    @Transactional
    @Query("update User u set u.name = :newName where u.email = :email")
    void updateUserName(@Param("newName") String newName, @Param("email") String email);
    @Modifying
    @Transactional
    @Query("update User u set u.password = :newPassword where u.email = :email")
    void updateUserPassword(@Param("newPassword") String newPassword, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("update User u set u.address = :newAddress where u.email = :email")
    void updateUserAddress(@Param("newAddress") String newAddress, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("update User u set u.role = :newRole where u.email = :email")
    void updateUserRole(@Param("newRole")Roles roles,@Param("email")String email);

    @Query("select u from User u")
    List<User>getAllUsers();
}
