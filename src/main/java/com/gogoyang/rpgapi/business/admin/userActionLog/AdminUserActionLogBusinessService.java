package com.gogoyang.rpgapi.business.admin.userActionLog;

import com.gogoyang.rpgapi.business.admin.common.IAdminCommonBusinessService;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;
import com.gogoyang.rpgapi.meta.userAction.service.IUserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminUserActionLogBusinessService implements IAdminUserActionLogBusinessService{
    private final IAdminCommonBusinessService iAdminCommonBusinessService;
    private final IUserActionLogService iUserActionLogService;
    private final IJobService iJobService;

    public AdminUserActionLogBusinessService(IAdminCommonBusinessService iAdminCommonBusinessService,
                                             IUserActionLogService iUserActionLogService,
                                             IJobService iJobService) {
        this.iAdminCommonBusinessService = iAdminCommonBusinessService;
        this.iUserActionLogService = iUserActionLogService;
        this.iJobService = iJobService;
    }

    /**
     * 查询用户行为记录
     * 统计用户行为记录总数
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

        for(int i=0;i<userActionLogs.size();i++){
            UserActionLog userActionLog=userActionLogs.get(i);
            Map map=new HashMap();
            Integer jobIdIndex=userActionLog.getMemo().indexOf("jobId");
            if(jobIdIndex!=-1){
                jobIdIndex+=6;
                String jobId=userActionLog.getMemo().substring(jobIdIndex,jobIdIndex+36);
                Job job=iJobService.getJobTinyByJobId(jobId);
                userActionLogs.get(i).setJobTitle(job.getTitle());
            }
        }

        Map out=new HashMap();
        out.put("userActionLogs", userActionLogs);

        /**
         * 统计记录总数
         */
        Integer total=iUserActionLogService.totalUserActionLog();

        out.put("totalUserActionLog", total);

        return out;
    }
}
