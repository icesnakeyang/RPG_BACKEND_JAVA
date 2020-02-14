package com.gogoyang.rpgapi.business.common;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;
import com.gogoyang.rpgapi.meta.userAction.service.IUserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonBusinessService implements ICommonBusinessService {
    private final IUserService iUserService;
    private final IRPGFunction irpgFunction;
    private final IUserActionLogService iUserActionLogService;
    private final IAdminService iAdminService;

    public CommonBusinessService(IUserService iUserService,
                                 IRPGFunction irpgFunction,
                                 IUserActionLogService iUserActionLogService,
                                 IAdminService iAdminService) {
        this.iUserService = iUserService;
        this.irpgFunction = irpgFunction;
        this.iUserActionLogService = iUserActionLogService;
        this.iAdminService = iAdminService;
    }

    @Override
    public User getUserByToken(String token) throws Exception {
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            //读取用户信息失败
            throw new Exception("10028");
        }
        return user;
    }

    /**
     * 记录用户行为日志
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public void createUserActionLog(Map in) throws Exception {
        GogoActType gogoActType = (GogoActType) in.get("GogoActType");
        Integer userId = (Integer) in.get("userId");
        String device = (String) in.get("device");
        String ipAddress = (String) in.get("ipAddress");
        HashMap memoMap = (HashMap) in.get("memo");
        String os = (String) in.get("os");
        String token = (String) in.get("token");

        User userInfo = null;
        if (userId != null) {
            userInfo = getUserByUserId(userId);
        } else {
            if (token != null) {
                userInfo = getUserByToken(token);
            }
        }

        UserActionLog userActLog = new UserActionLog();
        userActLog.setActType(gogoActType.toString());
        userActLog.setCreateTime(new Date());
        if (userInfo != null) {
            userActLog.setUserId(userInfo.getUserId().toString());
        }
        userActLog.setMemo(irpgFunction.convertMapToString(memoMap));
        userActLog.setUserActionLogId(irpgFunction.UUID().toString());
        iUserActionLogService.createUserActionLog(userActLog);
    }

    @Override
    public Admin getAdminByToken(String token) throws Exception {
        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("20001");
        }
        return admin;
    }

    public User getUserByUserId(Integer userId) throws Exception {
        User userInfo = iUserService.getUserByUserId(userId);
        if (userInfo == null) {
            throw new Exception("10028");
        }
        return userInfo;
    }
}
