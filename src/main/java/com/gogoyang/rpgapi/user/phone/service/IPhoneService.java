package com.gogoyang.rpgapi.user.phone.service;

import com.gogoyang.rpgapi.user.phone.entity.Phone;

import java.util.ArrayList;

public interface IPhoneService {
    Phone createPhone(Phone phone) throws Exception;

    void setPhoneDefault(String phone) throws Exception;

    ArrayList<Phone> loadPhoneByUserId(Integer userId) throws Exception;

}
