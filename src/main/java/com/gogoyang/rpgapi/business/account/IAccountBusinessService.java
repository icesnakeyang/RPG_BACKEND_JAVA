package com.gogoyang.rpgapi.business.account;


import java.util.Map;

public interface IAccountBusinessService {
    Map listMyAccount(Map in) throws Exception;

    Map loadAccountBalance(Map in) throws Exception;

    /**
     * 用户申请取现
     * @param in
     * @throws Exception
     */
    void withdraw(Map in) throws Exception;

    /**
     * 查询用户取现记录
     * @param in
     * @return
     * @throws Exception
     */
    Map listWithdraw(Map in) throws Exception;
}
