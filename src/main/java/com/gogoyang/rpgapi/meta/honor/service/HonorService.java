package com.gogoyang.rpgapi.meta.honor.service;

import com.gogoyang.rpgapi.meta.honor.dao.HonorDao;
import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class HonorService implements IHonorService {
    private final HonorDao honorDao;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public HonorService(HonorDao honorDao, IUserInfoService iUserInfoService) {
        this.honorDao = honorDao;
        this.iUserInfoService = iUserInfoService;
    }

    /**
     * 扣除用户的荣誉值
     * 1、创建HonorLog日志
     * 2、重新计算修改User的honorPoint
     *
     * @param honor
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deductHonor(Honor honor) throws Exception {

        //传入的荣誉值应该为负数，如果>0，则修改为负数
        Double point = (Double) honor.getPoint();
        if (point > 0) {
            point = -point;
        }
        honor.setPoint(point);
        honorDao.save(honor);

        //更新User的honorPoint
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(honor.getUserId());
        Map map = RefreshUserHonorPoint(honor.getUserId());
        userInfo.setHonor(Double.parseDouble(map.get("honor").toString()));
        userInfo.setHonorIn(Double.parseDouble(map.get("honorIn").toString()));
        userInfo.setHonorOut(Double.parseDouble(map.get("honorOut").toString()));
        iUserInfoService.updateUser(userInfo);

    }

    /**
     * 重新计算修改User的honorPoint
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    public Map RefreshUserHonorPoint(Integer userId) throws Exception {
        /**
         * 读取userId的所有HonorLog记录
         * 逐条计算point
         * 返回point
         */

        //todo

        Double honor = 0.0;
        Double honorIn = 0.0;
        Double honorOut = 0.0;

        Map out = new HashMap();
        out.put("honor", honor);
        out.put("honorIn", honorIn);
        out.put("honorOut", honorOut);

        return out;
    }
}
