package com.gogoyang.rpgapi.job.LOGICAL;

import java.util.Map;

/**
 * 显示job的详情页面
 * author：liuyang
 * 2018-10-5
 */
public interface IJobDetailPage {
    /**
     * 读取job的详情信息
     * @param jobId
     * @return
     * @throws Exception
     */
    Map loadJobDetail(Integer jobId) throws Exception;

    /**
     * 用户申请该job
     * @param in
     * @throws Exception
     */
    void applyJob(Map in) throws Exception;
}
