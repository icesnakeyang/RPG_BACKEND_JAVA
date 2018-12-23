package com.gogoyang.rpgapi.business.admin.secretary.user.service;

import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SecretaryUserBusinessService implements ISecretaryUserBusinessService{
    private final IUserInfoService iUserInfoService;
    private final IJobApplyService iJobApplyService;

    @Autowired
    public SecretaryUserBusinessService(IUserInfoService iUserInfoService,
                                        IJobApplyService iJobApplyService) {
        this.iUserInfoService = iUserInfoService;
        this.iJobApplyService = iJobApplyService;
    }

    /**
     * read one users apply history
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listApplyHistory(Map in) throws Exception {
        /**
         * input: pageIndex, pageSize, userId
         * output:
         *      createdTime: if contracted then contractTime, else createdTime of jobApply
         *      jobTitle: job.title
         *      jobPrice: job.price
         *      status: if contracted job status, else apply status
         *      spotNum: job.spotnum
         */
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Integer userId=(Integer)in.get("userId");

        UserInfo userInfo=iUserInfoService.getUserByToken(token);
        if(token==null){
            throw new Exception("10004");
        }

        Page<JobApply> jobApplies=iJobApplyService.listJobapplybyUserId(userId, pageIndex, pageSize);

        Map out=new HashMap();
        out.put("jobApply", jobApplies);
        return out;
    }
}
