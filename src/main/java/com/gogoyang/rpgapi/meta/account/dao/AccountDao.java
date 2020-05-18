package com.gogoyang.rpgapi.meta.account.dao;

import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface AccountDao {

    /**
     * 创建用户账户流水记录
     * @param account
     */
    void createAccount(Account account);

    /**
     * 读取一条账户流水记录
     * @param accountId
     * @return
     */
    Account getAccount(String accountId);

    /**
     * 查询用户账户流水记录
     * @param qIn
     * userId
     * jobId
     * offset
     * size
     * @return
     */
    ArrayList<Account> listAccount(Map qIn);

    /**
     * 分类汇总账户总额
     * @param qIn
     * userId
     * @return
     */
    ArrayList sumAccountByType(Map qIn);

    /**
     * 统计用户账户总数
     * @param qIn
     * userId
     * jobId
     * @return
     */
    Integer totalAccount(Map qIn);
}
