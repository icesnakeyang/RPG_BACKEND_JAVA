package com.gogoyang.rpgapi.meta.honor.service;

import com.gogoyang.rpgapi.meta.honor.dao.HonorDao;
import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
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


    @Override
    public void createHonor(Honor honor) {
        honorDao.createHonor(honor);
    }

    @Override
    public ArrayList<Honor> listHonor(Map qIn) {
        ArrayList<Honor> honors=honorDao.listHonor(qIn);
        return honors;
    }

    /**
     * 统计一个用户的荣誉值，可按type分类统计
     * @param qIn
     * userId
     * type
     * @return
     */
    @Override
    public Double sumHonor(Map qIn) {
        Double sum=honorDao.sumHonor(qIn);
        return sum;
    }
}
