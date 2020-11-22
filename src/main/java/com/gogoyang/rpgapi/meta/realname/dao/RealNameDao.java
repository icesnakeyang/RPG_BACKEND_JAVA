package com.gogoyang.rpgapi.meta.realname.dao;

import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface RealNameDao {
    /**
     * 创建用户实名信息
     *
     * @param realName
     */
    void createRealName(RealName realName);

    /**
     * 查询一个实名认证信息
     *
     * @param qIn userId
     *            idcardNo
     * @return
     */
    RealName getRealName(Map qIn);

    /**
     * 批量查询实名认证信息
     *
     * @param qIn verifyStatus
     *            offset
     *            size
     * @return
     */
    ArrayList<RealName> listRealName(Map qIn);

    /**
     * 统计实名信息数量
     *
     * @param qIn verifyStatus
     * @return
     */
    Integer totalRealName(Map qIn);

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
    void updateRealName(Map qIn);
}
