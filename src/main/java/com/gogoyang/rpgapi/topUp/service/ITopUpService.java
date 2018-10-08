package com.gogoyang.rpgapi.topUp.service;

import com.gogoyang.rpgapi.topUp.entity.TopUp;

import java.util.ArrayList;

public interface ITopUpService {
    /**
     * 新增一个充值记录
     * @param topUp
     * @return
     * @throws Exception
     */
    TopUp insertTopUp(TopUp topUp) throws Exception;

    /**
     * 读取所有用户的充值申请，processResult==null
     * @return
     * @throws Exception
     */
    ArrayList<TopUp> loadTopUpApply() throws Exception;

    /**
     * 读取单个用户的充值历史记录
     * @param userId
     * @return
     * @throws Exception
     */
    ArrayList<TopUp> loadTopUpHistory(Integer userId) throws Exception;
}
