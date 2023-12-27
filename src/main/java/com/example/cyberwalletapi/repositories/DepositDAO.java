package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.entities.DepositCodes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositDAO extends JpaRepository<DepositCodes, String> {
    @Query("select d from DepositCodes d where d.code=:code")
    DepositCodes getCode(@Param("code")String code);
    @Modifying
    @Transactional
    @Query("update User u set u.balance =u.balance+:amount ")
    Integer updateBalanceByCode(@Param("amount")Double amount);
}
