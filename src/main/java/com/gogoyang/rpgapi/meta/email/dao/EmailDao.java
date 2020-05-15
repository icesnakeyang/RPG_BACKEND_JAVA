package com.gogoyang.rpgapi.meta.email.dao;

import com.gogoyang.rpgapi.meta.email.entity.Email;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface EmailDao {
    void createEmail(Email email);

    /**
     * 查询email
     * @param qIn
     * email
     * userId
     * emailId
     * @return
     */
    Email getEmail(Map qIn);

    ArrayList<Email> listEmail(Map qIn);

    void updateEmail(Email email);
}
