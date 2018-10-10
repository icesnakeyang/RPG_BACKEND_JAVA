package com.gogoyang.rpgapi.meta.user.phone.service;

import com.gogoyang.rpgapi.meta.user.phone.dao.PhoneDao;
import com.gogoyang.rpgapi.meta.user.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class PhoneService implements IPhoneService {
    private final PhoneDao phoneDao;

    @Autowired
    public PhoneService(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Phone insertPhone(Phone phone) throws Exception {
        phone = phoneDao.save(phone);
        return phone;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setPhoneDefault(String phone) throws Exception {
        Phone thePhone = phoneDao.findByPhone(phone);
        thePhone.setDefault(true);
        phoneDao.save(thePhone);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(thePhone.getUserId());
        userInfo.setPhone(thePhone.getPhone());
    }

    @Override
    public ArrayList<Phone> loadPhoneByUserId(Integer userId) throws Exception {
        ArrayList<Phone> phones = phoneDao.findAllByUserId(userId);

        return phones;
    }

    @Override
    public Phone loadPhoneByPhone(String phone) throws Exception {
        Phone thePhone=phoneDao.findByPhone(phone);
        return thePhone;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updatePhone(Phone phone) throws Exception {
        if(phone.getPhoneId()==null){
            throw new Exception("10045");
        }
        phoneDao.save(phone);
    }
}
