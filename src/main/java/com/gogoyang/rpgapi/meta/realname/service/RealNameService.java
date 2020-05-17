package com.gogoyang.rpgapi.meta.realname.service;

import com.gogoyang.rpgapi.meta.realname.dao.RealNameDao;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RealNameService implements IRealNameService{
    private final RealNameDao realNameDao;

    @Autowired
    public RealNameService(RealNameDao realNameDao) {
        this.realNameDao = realNameDao;
    }

    @Override
    public void insert(RealName realName) throws Exception {
        if(realName.getRealName()!=null){
            throw new Exception("10047");
        }
        realNameDao.createRealName(realName);
    }

    @Override
    public void update(RealName realName) throws Exception {
        realNameDao.updateRealName(realName);
    }

    @Override
    public RealName getRealNameByUserId(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        RealName realName=realNameDao.getRealName(qIn);
        return realName;
    }
}
