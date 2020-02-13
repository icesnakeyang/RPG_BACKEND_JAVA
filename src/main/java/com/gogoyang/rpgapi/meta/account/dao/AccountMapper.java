package com.gogoyang.rpgapi.meta.account.dao;

import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {

    /**
     * 创建一个取现日志
     * @param withdrawLedger
     */
    void createWithdrawLedger(WithdrawLedger withdrawLedger);

}
