package com.gogoyang.rpgapi.meta.phone.service;

import com.gogoyang.rpgapi.meta.phone.dao.PhoneDao;
import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PhoneService implements IPhoneService{
    private final PhoneDao phoneDao;

    @Autowired
    public PhoneService(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insert(Phone phone) throws Exception {
        if(phone.getPhoneId()!=null){
            throw new Exception("10115");
        }
        phoneDao.createPhone(phone);
    }

    @Override
    public Phone getPhoneByPhone(String strPhone) throws Exception {
        Map qIn=new HashMap();
        qIn.put("phone", strPhone);
        Phone phone=phoneDao.getPhone(qIn);
        return phone;
    }

    @Override
    public ArrayList<Phone> listPhoneByUserId(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        ArrayList<Phone> phones=phoneDao.listPhone(qIn);
        return phones;
    }

    @Override
    public Phone getDefaultPhoneByUserId(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        qIn.put("isDefault", true);
        Phone phone=phoneDao.getPhone(qIn);
        return phone;
    }
}
