package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.dao.AccountMapper;
import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawLedgerService implements IWithdrawLedgerService{
    private final AccountMapper accountMapper;

    public WithdrawLedgerService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    /**
     * 创建一个取现日志
     * @param withdrawLedger
     */
    @Override
    public void createWithdrawLedger(WithdrawLedger withdrawLedger) {
        accountMapper.createWithdrawLedger(withdrawLedger);
    }
}
