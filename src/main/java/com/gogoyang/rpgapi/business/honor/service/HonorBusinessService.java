package com.gogoyang.rpgapi.business.honor.service;

import com.gogoyang.rpgapi.business.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.HonorType;
import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import com.gogoyang.rpgapi.meta.honor.service.IHonorService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class HonorBusinessService implements IHonorBusinessService{
    private final IHonorService iHonorService;
    private final IUserService iUserService;
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public HonorBusinessService(IHonorService iHonorService,
                                IUserService iUserService,
                                ICommonBusinessService iCommonBusinessService) {
        this.iHonorService = iHonorService;
        this.iUserService = iUserService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Override
    public Map listMyHonor(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        UserInfo user=iCommonBusinessService.getUserByToken(token);

        Map qIn=new HashMap();
        qIn.put("userId", user.getUserId());
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Honor> honors=iHonorService.listHonor(qIn);

        Map out=new HashMap();
        out.put("honors", honors);
        return out;
    }

    /**
     * 统计我的honor的值
     * honorIn
     * honorOut
     * honorBalance
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadUserHonorBalance(Map in) throws Exception {
        String token=in.get("token").toString();

        UserInfo user=iCommonBusinessService.getUserByToken(token);

        Map out=loadUserHonorBalance(user.getUserId());

        return out;
    }

    /**
     * 重新计算修改User的honorPoint
     *
     * @param userId
     * @return
     * @throws Exception
     */
    private Map loadUserHonorBalance(String userId) throws Exception {
        /**
         * 读取userId的所有HonorLog记录
         * 逐条计算point
         * 返回point
         */

        Map qIn=new HashMap();
        qIn.put("userId", userId);
        qIn.put("type", HonorType.JOB_ACCEPTED);
        Integer totalAcceptance=iHonorService.sumHonor(qIn);

        qIn.put("type", HonorType.CREATE_SPOTLIGHT);
        Integer totalSpotlight=iHonorService.sumHonor(qIn);

        if(totalAcceptance==null){
            totalAcceptance=0;
        }
        if(totalSpotlight==null){
            totalSpotlight=0;
        }
        Integer honorIn=totalAcceptance;
        Integer honorOut=totalSpotlight;
        Integer honor=honorIn-honorOut;

        Map out = new HashMap();
        out.put("honor", honor);
        out.put("honorIn", honorIn);
        out.put("honorOut", honorOut);

        UserInfo userInfo=new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setHonor(honor);
        userInfo.setHonorIn(honorIn);
        userInfo.setHonorOut(honorOut);

        iUserService.updateUserInfo(userInfo);

        return out;
    }
}
