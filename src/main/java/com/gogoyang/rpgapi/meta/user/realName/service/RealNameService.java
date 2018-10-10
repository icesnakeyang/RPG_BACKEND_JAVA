package com.gogoyang.rpgapi.meta.user.realName.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.gogoyang.rpgapi.meta.user.realName.dao.RealNameDao;
import com.gogoyang.rpgapi.meta.user.realName.entity.RealName;
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
    public RealName insertRealName(RealName name) throws Exception{
        RealName realName=realNameDao.save(name);
        return realName;
    }

    @Override
    public ArrayList<RealName> loadRealNameByUserId(Integer userId) throws Exception{
        ArrayList<RealName> realNames=realNameDao.findAllByUserId(userId);
        return realNames;
    }

    /**
     * 读取当前有效的实名
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public RealName loadCurrentRealName(Integer userId) throws Exception {
        RealName realName=realNameDao.findByUserIdAndDisableTimeIsNull(userId);
        return realName;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateRealName(RealName realName) throws Exception {
        if(realName.getRealNameId()==null){
            throw new Exception("10047");
        }
        realNameDao.save(realName);
    }


}
