package com.gogoyang.rpgapi.business.honor.service;

import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import com.gogoyang.rpgapi.meta.honor.service.IHonorService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class HonorBusinessService implements IHonorBusinessService{
    private final IHonorService iHonorService;
    private final IUserService iUserService;

    @Autowired
    public HonorBusinessService(IHonorService iHonorService,
                                IUserService iUserService) {
        this.iHonorService = iHonorService;
        this.iUserService = iUserService;
    }

    @Override
    public Map listMyHonor(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        Page<Honor> honors=iHonorService.listMyHonor(user.getUserId(), pageIndex, pageSize);
        ArrayList list=new ArrayList();
        for(int i=0;i<honors.getContent().size();i++){
            Honor honor=honors.getContent().get(i);
            Map map=new HashMap();
            map.put("honorId", honor.getHonorId());
            map.put("userId", honor.getUserId());
            if(user.getRealName()!=null) {
                map.put("userName", user.getRealName());
            }else {
                map.put("userName", user.getEmail());
            }
            map.put("point", honor.getPoint());
            map.put("jobId", honor.getJobId());
            map.put("createdTime", honor.getCreatedTime());
            map.put("reamrk", honor.getRemark());
            list.add(map);
        }
        Map out=new HashMap();
        out.put("list", list);
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
    public Map loadMyHonorBalance(Map in) throws Exception {
        String token=in.get("token").toString();
        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        return null;
    }
}
