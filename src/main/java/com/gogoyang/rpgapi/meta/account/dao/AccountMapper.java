package com.gogoyang.rpgapi.meta.account.dao;

import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface AccountMapper {

    /**
     * 创建一个取现日志
     * @param withdrawLedger
     */
    void createWithdrawLedger(WithdrawLedger withdrawLedger);

    /**
     * 查询用户取现记录
     * @param qIn
     * userId
     * offset
     * size
     * @return
     */
    ArrayList<WithdrawLedger> listWithdraw(Map qIn);

    /**
     * 统计取现记录总数
     * @param qIn
     * {userId}
     * @return
     */
    Integer totalWithdraw(Map qIn);

}
