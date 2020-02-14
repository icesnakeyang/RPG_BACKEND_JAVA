package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.dao.AccountMapper;
import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class WithdrawLedgerService implements IWithdrawLedgerService {
    private final AccountMapper accountMapper;

    public WithdrawLedgerService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    /**
     * 创建一个取现日志
     *
     * @param withdrawLedger
     */
    @Override
    public void createWithdrawLedger(WithdrawLedger withdrawLedger) throws Exception {
        accountMapper.createWithdrawLedger(withdrawLedger);
    }

    /**
     * 查询用户取现记录
     *
     * @param qIn userId
     *            offset
     *            size
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<WithdrawLedger> listWithdraw(Map qIn) throws Exception {
        ArrayList<WithdrawLedger> withdrawLedgers = accountMapper.listWithdraw(qIn);
        return withdrawLedgers;
    }

    @Override
    public ArrayList<Map<String, Object>> listWithdrawAdmin(Map qIn) throws Exception {
        ArrayList<Map<String, Object>> list=accountMapper.listWithdrawAdmin(qIn);
        return list;
    }

    /**
     * 统计取现记录总数
     *
     * @param qIn {userId}
     * @return
     * @throws Exception
     */
    @Override
    public Integer totalWithdraw(Map qIn) throws Exception {
        Integer total = accountMapper.totalWithdraw(qIn);
        return total;
    }
}
