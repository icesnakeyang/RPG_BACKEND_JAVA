package com.gogoyang.rpgapi.business.job.repeal.service;

import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;

import java.util.Map;

public class RepealBusinessService implements IRepealBusinessService {
    @Override
    public Repeal createRepeal(Map in) throws Exception {
        /**
         * 创建一个撤诉申请
         * 根据token获取当前用户
         * 根据jobId获取当前任务
         * 只有甲方或者乙方能创建撤诉申请
         * 检查当前任务是否处于申诉状态
         * 检查当前是否有未处理的申请
         * 创建撤诉申请
         */
        return null;
    }
}
