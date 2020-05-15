package com.gogoyang.rpgapi.meta.phone.dao;

import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface PhoneDao {
    void createPhone(Phone phone);

    /**
     * 查询手机号码
     * @param qIn
     * phone
     * userId
     * phoneId
     * isVerify
     * @return
     */
    Phone getPhone(Map qIn);

    /**
     * 批量查询手机号码
     * @param qIn
     * userId
     * isVerify
     * @return
     */
    ArrayList<Phone> listPhone(Map qIn);
}
