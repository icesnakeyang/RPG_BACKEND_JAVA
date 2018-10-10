package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.dao.AccountDao;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class AccountService implements IAccountService{
    private final AccountDao accountDao;

    @Autowired
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * save a new account record
     * @param account
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Account insertNewAccount(Account account) throws Exception {
        if(account.getAccountId()!=null){
            throw new Exception("10042");
        }
        account=accountDao.save(account);
        return account;
    }

    /**
     * calculate user balance
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public Map refreshAccountBalance(Integer userId) throws Exception {
        /**
         * load all userId account record
         * calculate the total income, total outgoing, and balance
         * return map
         */
        return null;
    }
}
