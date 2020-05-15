package com.gogoyang.rpgapi.meta.account.dao;

import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface AccountDao {

    void createAccount(Account account);

    Account getAccount(String accountId);

    ArrayList<Account> listAccount(Map qIn);
}
