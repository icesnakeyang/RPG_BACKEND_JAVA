package com.gogoyang.rpgapi.meta.realname.service;

import com.gogoyang.rpgapi.meta.realname.entity.RealName;

import java.util.ArrayList;
import java.util.Map;

public interface IRealNameService {
    void insert(RealName realName) throws Exception;

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
    void update(Map qIn) throws Exception;

    /**
     * 批量查询实名认证信息
     *
     * @param qIn verifyStatus
     *            offset
     *            size
     * @return
     */
    ArrayList<RealName> listRealName(Map qIn) throws Exception;

    /**
     * 统计实名信息数量
     * @param qIn
     * verifyStatus
     * @return
     */
    Integer totalRealName(Map qIn);

    RealName getRealNameByUserId(String userId) throws Exception;
}
