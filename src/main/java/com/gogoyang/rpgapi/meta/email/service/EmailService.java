package com.gogoyang.rpgapi.meta.email.service;

import com.gogoyang.rpgapi.meta.email.dao.EmailDao;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class EmailService implements IEmailService{
    private final EmailDao emailDao;

    @Autowired
    public EmailService(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    @Override
    public Email getEmailByEmail(String emailName) throws Exception {
        Email email=emailDao.findByEmail(emailName);
        return email;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insert(Email email) throws Exception {
        if(email.getEmailId()!=null){
            throw new Exception("10023");
        }
        emailDao.save(email);
    }

    @Override
    public ArrayList<Email> listEmailByUserId(Integer userId) throws Exception {
        ArrayList<Email> emails=emailDao.findAllByUserId(userId);
        return emails;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateEmail(Email email) throws Exception {
        if(email.getEmailId()==null){
            throw new Exception("10114");
        }
        emailDao.save(email);
    }

    @Override
    public Email getDefaultEmailByUserId(Integer userId) throws Exception {
        Email email=emailDao.findByUserIdAndIsDefaultIsTrue(userId);
        return email;
    }
}
