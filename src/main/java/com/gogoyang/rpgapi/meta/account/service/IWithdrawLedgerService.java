package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;

public interface IWithdrawLedgerService {
    /**
     * 创建一个取现日志
     * @param withdrawLedger
     */
    void createWithdrawLedger(WithdrawLedger withdrawLedger);
}
