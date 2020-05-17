package com.gogoyang.rpgapi.meta.realname.dao;

import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface RealNameDao {
    /**
     * 创建用户实名信息
     * @param realName
     */
    void createRealName(RealName realName);

    /**
     * 查询一个实名认证信息
     * @param qIn
     * userId
     * idcardNo
     * @return
     */
    RealName getRealName(Map qIn);

    /**
     * 批量查询实名认证信息
     * @param qIn
     * offset
     * size
     * @return
     */
    ArrayList<RealName> listRealName(Map qIn);

    /**
     * 根据userId修改用户实名信息
     * @param realName
     */
    void updateRealName(RealName realName);
}
