package com.gogoyang.rpgapi.account.service;

import com.gogoyang.rpgapi.account.entity.Account;

import java.util.Map;

public interface IAccountService {
    Account insertNewAccount(Account account) throws Exception;

    Map refreshAccountBalance(Integer userId) throws Exception;
}
