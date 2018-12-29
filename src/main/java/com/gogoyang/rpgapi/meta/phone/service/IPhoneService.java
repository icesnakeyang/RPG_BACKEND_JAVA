package com.gogoyang.rpgapi.meta.phone.service;

import com.gogoyang.rpgapi.meta.phone.entity.Phone;

import java.util.ArrayList;

public interface IPhoneService {
    void insert(Phone phone) throws Exception;
    Phone getPhoneByPhone(String strPhone) throws Exception;
    ArrayList<Phone> listPhoneByUserId(Integer userId) throws Exception;
    void updatePhone(Phone phone) throws Exception;
    Phone getDefaultPhoneByUserId(Integer userId) throws Exception;
}
