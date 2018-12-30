package com.gogoyang.rpgapi.business.admin.secretary.user.service;

import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SecretaryUserBusinessService implements ISecretaryUserBusinessService{
    private final IJobApplyService iJobApplyService;
    private final IUserService iUserService;

    @Autowired
    public SecretaryUserBusinessService(IJobApplyService iJobApplyService,
                                        IUserService iUserService) {
        this.iJobApplyService = iJobApplyService;
        this.iUserService = iUserService;
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

        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        Page<JobApply> jobApplies=iJobApplyService.listJobapplybyUserId(userId, pageIndex, pageSize);

        Map out=new HashMap();
        out.put("jobApply", jobApplies);
        return out;
    }
}
