package com.gogoyang.rpgapi.meta.account.service;

import com.gogoyang.rpgapi.meta.account.entity.WithdrawLedger;

import java.util.ArrayList;
import java.util.Map;

public interface IWithdrawLedgerService {
    /**
     * 创建一个取现日志
     *
     * @param withdrawLedger
     */
    void createWithdrawLedger(WithdrawLedger withdrawLedger) throws Exception;

    /**
     * 查询用户取现记录
     *
     * @param qIn userId
     *            offset
     *            size
     * @return
     */
    ArrayList<WithdrawLedger> listWithdraw(Map qIn) throws Exception;

    /**
     * 管理员查询用户的取现申请
     *
     * @param qIn
     * @return
     * @throws Exception
     */
    ArrayList<Map<String, Object>> listWithdrawAdmin(Map qIn) throws Exception;

    /**
     * 统计取现记录总数
     *
     * @param qIn {userId}
     * @return
     */
    Integer totalWithdraw(Map qIn) throws Exception;

    /**
     * 管理员读取取现申请详情
     *
     * @param withdrawLedgerId
     * @return
     */
    WithdrawLedger getWithdrawAdmin(String withdrawLedgerId) throws Exception;

    /**
     * 修改取现申请
     * @param qIn
     * status
     * readTime
     * processTime
     * processRemark
     * processReadTime
     * withdrawLedgerId
     */
    void updateWithdraw(Map qIn) throws Exception;

}
