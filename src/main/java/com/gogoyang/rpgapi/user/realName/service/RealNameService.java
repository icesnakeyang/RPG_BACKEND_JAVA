package com.gogoyang.rpgapi.user.realName.service;

import com.gogoyang.rpgapi.user.realName.dao.RealNameDao;
import com.gogoyang.rpgapi.user.realName.entity.RealName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class RealNameService implements IRealNameService{
    private final RealNameDao realNameDao;

    @Autowired
    public RealNameService(RealNameDao realNameDao) {
        this.realNameDao = realNameDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public RealName createName(RealName name) {
        RealName realName=realNameDao.save(name);
        return realName;
    }

    @Override
    public ArrayList<RealName> loadByUserId(Integer userId) {
        ArrayList<RealName> realNames=realNameDao.findAllByUserId(userId);
        return realNames;
    }
}