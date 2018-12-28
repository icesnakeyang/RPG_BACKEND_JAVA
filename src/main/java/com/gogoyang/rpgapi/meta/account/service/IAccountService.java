package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IAccountService {
    Account insertNewAccount(Account account) throws Exception;

    Map loadAccountBalance(Integer userId) throws Exception;

    /**
     * Read my account data
     * @param qIn
     * {
     *     userId:
     *     pageIndex:
     *     pageSize:
     * }
     * @return
     * @throws Exception
     */
    Page listMyAccount(Map qIn) throws Exception;
}
