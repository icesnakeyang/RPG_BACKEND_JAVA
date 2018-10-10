package com.gogoyang.rpgapi.meta.job.service;

import java.util.Map;

/**
 * 显示job的详情页面
 * author：liuyang
 * 2018-10-5
 */
public interface IJobDetail {
    /**
     * 读取job的详情信息
     * @param in
     * @return
     * @throws Exception
     */
    Map loadJobDetail(Map in) throws Exception;

    /**
     * 用户申请该job
     * @param in
     * @throws Exception
     */
    void applyJob(Map in) throws Exception;

    void agreeMatch(Map in) throws Exception;

    void rejectMatch(Map in) throws Exception;
}
