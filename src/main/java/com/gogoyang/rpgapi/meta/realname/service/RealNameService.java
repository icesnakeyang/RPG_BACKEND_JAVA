package com.gogoyang.rpgapi.meta.realname.service;

import com.gogoyang.rpgapi.meta.realname.dao.RealNameDao;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class RealNameService implements IRealNameService {
    private final RealNameDao realNameDao;

    @Autowired
    public RealNameService(RealNameDao realNameDao) {
        this.realNameDao = realNameDao;
    }

    @Override
    public void insert(RealName realName) throws Exception {
        realNameDao.createRealName(realName);
    }

    /**
     * 根据userId修改用户实名信息
     *
     * @param qIn realName
     *            status
     *            idcardNo
     *            sex
     *            verifyResult
     *            remark
     *            userId
     */
    @Override
    public void update(Map qIn) throws Exception {
        realNameDao.updateRealName(qIn);
    }

    /**
     * 批量查询实名认证信息
     *
     * @param qIn verifyStatus
     *            offset
     *            size
     * @return
     */
    @Override
    public ArrayList<RealName> listRealName(Map qIn) throws Exception {
        ArrayList<RealName> realNames = realNameDao.listRealName(qIn);
        return realNames;
    }

    /**
     * 统计实名信息数量
     * @param qIn
     * verifyStatus
     * @return
     */
    @Override
    public Integer totalRealName(Map qIn) {
        Integer total = realNameDao.totalRealName(qIn);
        return total;
    }

    @Override
    public RealName getRealNameByUserId(String userId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("userId", userId);
        RealName realName = realNameDao.getRealName(qIn);
        return realName;
    }
}
