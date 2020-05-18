package com.gogoyang.rpgapi.business.job.common;

import java.util.Map;

public interface IJobCommonBusinessService {
    /**
     * 统计我的所有任务未读信息
     * 包括所有任务的所有日志，完成，终止，申诉
     * @param in
     * @return
     * @throws Exception
     */
    Map totalMyUnread(Map in) throws Exception;

    /**
     * 统计我的任务日志总数
     * @param in
     * @return
     * @throws Exception
     */
    Map totalMyLog(Map in) throws Exception;

    /**
     * 读取一个任务的简要信息
     * @param in
     * @return
     * @throws Exception
     */
    Map getJobTinyByJobId(Map in) throws Exception;
}
