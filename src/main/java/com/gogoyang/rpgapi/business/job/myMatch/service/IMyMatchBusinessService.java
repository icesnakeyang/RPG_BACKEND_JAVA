package com.gogoyang.rpgapi.business.job.myMatch.service;

import java.util.Map;

public interface IMyMatchBusinessService {
    /**
     * 读取所有分配给我的新任务
     * @param in
     * @return
     * @throws Exception
     */
    Map loadJobMatchToMe(Map in) throws Exception;

    /**
     * 接受新任务
     * @param in
     * @throws Exception
     */
    void acceptNewJob(Map in) throws Exception;

    void rejectNewJob(Map in) throws Exception;
}
