package com.gogoyang.rpgapi.business.account.service;

import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IAccountBusinessService {
    Page<Account> listMyAccount(Map in) throws Exception;

    Map loadAccountBalance(Map in) throws Exception;
}
