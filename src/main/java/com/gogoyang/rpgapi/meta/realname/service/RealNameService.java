package com.gogoyang.rpgapi.meta.realname.service;

import com.gogoyang.rpgapi.meta.realname.dao.RealNameDao;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RealNameService implements IRealNameService{
    private final RealNameDao realNameDao;

    @Autowired
    public RealNameService(RealNameDao realNameDao) {
        this.realNameDao = realNameDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insert(RealName realName) throws Exception {
        if(realName.getRealNameId()!=null){
            throw new Exception("10047");
        }
        realNameDao.save(realName);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(RealName realName) throws Exception {
        if(realName.getRealNameId()==null){
            throw new Exception("10047");
        }
        realNameDao.save(realName);
    }

    @Override
    public RealName getRealNameByUserId(Integer userId) throws Exception {
        RealName realName=realNameDao.findByUserId(userId);
        return realName;
    }
}
