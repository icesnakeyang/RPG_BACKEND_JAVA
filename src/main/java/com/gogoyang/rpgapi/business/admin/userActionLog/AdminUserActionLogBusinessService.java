package com.gogoyang.rpgapi.business.admin.userActionLog;

import com.gogoyang.rpgapi.business.admin.common.IAdminCommonBusinessService;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;
import com.gogoyang.rpgapi.meta.userAction.service.IUserActionLogService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminUserActionLogBusinessService implements IAdminUserActionLogBusinessService{
    private final IAdminCommonBusinessService iAdminCommonBusinessService;
    private final IUserActionLogService iUserActionLogService;

    public AdminUserActionLogBusinessService(IAdminCommonBusinessService iAdminCommonBusinessService,
                                             IUserActionLogService iUserActionLogService) {
        this.iAdminCommonBusinessService = iAdminCommonBusinessService;
        this.iUserActionLogService = iUserActionLogService;
    }

    /**
     * 查询用户行为记录
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listUserActionLog(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        Admin admin=iAdminCommonBusinessService.getAdminUserByToken(token);

        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);

        ArrayList<UserActionLog> userActionLogs=iUserActionLogService.listUserActionLog(qIn);

        Map out=new HashMap();
        out.put("userActionLogs", userActionLogs);
        return out;
    }
}
