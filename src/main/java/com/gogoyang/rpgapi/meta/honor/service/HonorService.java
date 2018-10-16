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
     * 新增一个Honor记录
     * @param honor
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insertHonor(Honor honor) throws Exception {
        if(honor.getHonorLogId()!=null){
            throw new Exception("10062");
        }
        honorDao.save(honor);
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
