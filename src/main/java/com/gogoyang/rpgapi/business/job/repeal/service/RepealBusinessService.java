package com.gogoyang.rpgapi.business.job.repeal.service;

import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;

@Service
public class RepealBusinessService implements IRepealBusinessService {
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Repeal createRepeal(Map in) throws Exception {
        /**
         * 创建一个撤诉申请
         * 根据token获取当前用户
         * 根据jobId获取当前任务
         * 只有甲方或者乙方能创建撤诉申请
         * 检查当前任务是否处于申诉状态
         * 检查当前是否有未处理的申请
         * 创建撤诉申请，jobstatus=SPOTLIGHTING
         */
        return null;
    }

    @Override
    public ArrayList<Repeal> listRepeal(Map in) throws Exception {
        /**
         * 读取jobId的所有Repeal
         * 把userId的name补充完整
         */
        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setRepealReadTime(Map in) throws Exception {
        /**
         * 读取jobId，userId的未读repeal
         * 设置readTime为当前服务器时间
         * 保存
         */

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectRepeal(Map in) throws Exception {
        /**
         * 把repeal.processResult设置为reject
         * 设置处理用户和处理时间
         */

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptRepeal(Map in) throws Exception {
        /**
         * 同意撤诉
         * 1、处理撤诉repeal.processResult=accept
         * 2、处理spotlight，jobstatus=SPOTCANCELLED
         */
    }

    @Override
    public Integer countUnreadRepeal(Map in) throws Exception {
        /**
         * 读取当前userId，jobId的未阅读的repeal
         * 统计返回数量
         */
        return null;
    }
}
