package com.gogoyang.rpgapi.meta.phone.service;

import com.gogoyang.rpgapi.meta.phone.dao.PhoneDao;
import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

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
        phoneDao.save(phone);
    }

    @Override
    public Phone getPhoneByPhone(String strPhone) throws Exception {
        Phone phone=phoneDao.findByPhone(strPhone);
        return phone;
    }

    @Override
    public ArrayList<Phone> listPhoneByUserId(Integer userId) throws Exception {
        ArrayList<Phone> phones=phoneDao.findAllByUserId(userId);
        return phones;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updatePhone(Phone phone) throws Exception {
        if(phone.getPhoneId()==null){
            throw new Exception("10116");
        }
        phoneDao.save(phone);
    }
}
