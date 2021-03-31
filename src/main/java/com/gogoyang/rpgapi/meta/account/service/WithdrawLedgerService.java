package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.dao.WithdrawLedgerDao;
import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class WithdrawLedgerService implements IWithdrawLedgerService {
    private final WithdrawLedgerDao withdrawLedgerDao;

    public WithdrawLedgerService(WithdrawLedgerDao withdrawLedgerDao) {
        this.withdrawLedgerDao = withdrawLedgerDao;
    }

    /**
     * 创建一个取现日志
     *
     * @param withdrawLedger
     */
    @Override
    public void createWithdrawLedger(WithdrawLedger withdrawLedger) throws Exception {
        withdrawLedgerDao.createWithdrawLedger(withdrawLedger);
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
        ArrayList<WithdrawLedger> withdrawLedgers = withdrawLedgerDao.listWithdraw(qIn);
        return withdrawLedgers;
    }

    @Override
    public ArrayList<Map<String, Object>> listWithdrawAdmin(Map qIn) throws Exception {
        ArrayList<Map<String, Object>> list = withdrawLedgerDao.listWithdrawAdmin(qIn);
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
        Integer total = withdrawLedgerDao.totalWithdraw(qIn);
        return total;
    }

    @Override
    public WithdrawLedger getWithdrawAdmin(String withdrawLedgerId) throws Exception {
        WithdrawLedger withdrawLedger = withdrawLedgerDao.getWithdrawAdmin(withdrawLedgerId);
        return withdrawLedger;
    }

    @Override
    public void updateWithdraw(Map qIn) throws Exception {
        withdrawLedgerDao.updateWithdraw(qIn);
    }
}
