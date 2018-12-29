package com.gogoyang.rpgapi.meta.honor.service;

import com.gogoyang.rpgapi.meta.honor.dao.HonorDao;
import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class HonorService implements IHonorService {
    private final HonorDao honorDao;
    private final IUserService iUserService;

    @Autowired
    public HonorService(HonorDao honorDao,
                        IUserService iUserService) {
        this.honorDao = honorDao;
        this.iUserService = iUserService;
    }

    /**
     * 新增一个Honor记录
     * @param honor
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insertHonor(Honor honor) throws Exception {
        if(honor.getHonorId()!=null){
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
    public Map refreshUserHonorPoint(Integer userId) throws Exception {
        /**
         * 读取userId的所有HonorLog记录
         * 逐条计算point
         * 返回point
         */

        //todo

        Integer honor=0;
        Integer honorIn=0;
        Integer honorOut=0;

        Map out = new HashMap();
        out.put("honor", honor);
        out.put("honorIn", honorIn);
        out.put("honorOut", honorOut);

        return out;
    }
}
