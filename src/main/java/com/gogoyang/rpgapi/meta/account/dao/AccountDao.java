package com.gogoyang.rpgapi.meta.account.dao;

import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDao extends JpaRepository<Account, Integer>{
}
