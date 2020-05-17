package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.entity.Account;

import java.util.ArrayList;
import java.util.Map;

public interface IAccountService {
    /**
     * 创建用户账户流水记录
     * @param account
     */
    void createAccount(Account account);

    /**
     * 读取一条账户流水记录
     * @param accountId
     * @return
     */
    Account getAccount(String accountId);

    /**
     * 查询用户账户流水记录
     * @param qIn
     * userId
     * jobId
     * offset
     * size
     * @return
     */
    ArrayList<Account> listAccount(Map qIn);

    /**
     * 分类汇总账户总额
     * @param qIn
     * userId
     * @return
     */
    ArrayList sumAccountByType(Map qIn);
}
