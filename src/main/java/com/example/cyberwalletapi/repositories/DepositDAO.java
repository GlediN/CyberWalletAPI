package com.example.cyberwalletapi.repositories;

import com.example.cyberwalletapi.entities.DepositCodes;
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
    @Query("update User u set u.balance =:amount where u.email=:email")
    void updateBalanceByCode(@Param("amount") Double amount,@Param("email")String email);
    @Query("select d from DepositCodes d where d.code=:code")
    boolean doesCodeExist(@Param("code")String code);
    @Modifying
    @Query("delete from DepositCodes where code=:code")
    void deleteCodeAfterUse(@Param("code")String code);

}
