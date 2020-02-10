package com.gogoyang.rpgapi.business.admin.userActionLog;

import java.util.Map;

public interface IAdminUserActionLogBusinessService {
    /**
     * 查询用户行为记录
     * 统计用户行为记录总数
     * @param in
     * @return
     * @throws Exception
     */
    Map listUserActionLog(Map in) throws Exception;
}
