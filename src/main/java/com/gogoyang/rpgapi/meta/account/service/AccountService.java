package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.dao.AccountDao;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class AccountService implements IAccountService {
    private final AccountDao accountDao;

    @Autowired
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @Override
    public void createAccount(Account account) {
        accountDao.createAccount(account);
    }

    @Override
    public Account getAccount(String accountId) {
        Account account=accountDao.getAccount(accountId);
        return account;
    }

    @Override
    public ArrayList<Account> listAccount(Map qIn) {
        ArrayList<Account> accounts=accountDao.listAccount(qIn);
        return accounts;
    }

    /**
     * 分类汇总账户总额
     * @param qIn
     * userId
     * @return
     */
    @Override
    public ArrayList sumAccountByType(Map qIn) {
        ArrayList out=accountDao.sumAccountByType(qIn);
        return out;
    }
}
