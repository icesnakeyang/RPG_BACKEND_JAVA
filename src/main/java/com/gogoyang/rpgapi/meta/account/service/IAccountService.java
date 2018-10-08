package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.entity.Account;

import java.util.Map;

public interface IAccountService {
    Account insertNewAccount(Account account) throws Exception;

    Map refreshAccountBalance(Integer userId) throws Exception;
}
