package com.gogoyang.rpgapi.meta.account.dao;

import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface AccountDao extends JpaRepository<Account, Integer>{
    ArrayList<Account> findAllByUserId(Integer userId);
}
