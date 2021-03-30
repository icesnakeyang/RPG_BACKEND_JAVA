package com.gogoyang.rpgapi.business.admin.secretary.maintenance;

import java.util.Map;

public interface IMaintenanceBService {
    /**
     * 检查未成交任务时间，把超过期限的任务设置为过期
     * @param in
     * @throws Exception
     */
    void overdueJobs(Map in) throws Exception;
}
