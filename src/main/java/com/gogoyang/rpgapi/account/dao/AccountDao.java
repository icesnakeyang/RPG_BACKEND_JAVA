package com.gogoyang.rpgapi.account.dao;

import com.gogoyang.rpgapi.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDao extends JpaRepository<Account, Integer>{
}
