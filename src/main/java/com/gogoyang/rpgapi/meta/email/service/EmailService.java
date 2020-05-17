package com.gogoyang.rpgapi.meta.email.service;

import com.gogoyang.rpgapi.meta.email.dao.EmailDao;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService implements IEmailService{
    private final EmailDao emailDao;

    @Autowired
    public EmailService(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    @Override
    public Email getEmailByEmail(String emailName) throws Exception {
        Map qIn=new HashMap();
        qIn.put("email", emailName);
        Email email=emailDao.getEmail(qIn);
        return email;
    }

    @Override
    public void insert(Email email) throws Exception {
        if(email.getEmailId()!=null){
            throw new Exception("10023");
        }
        emailDao.createEmail(email);
    }

    @Override
    public ArrayList<Email> listEmailByUserId(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        ArrayList<Email> emails=emailDao.listEmail(qIn);
        return emails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(Email email) throws Exception {
        if(email.getEmailId()==null){
            throw new Exception("10114");
        }
        emailDao.updateEmail(email);
    }

    @Override
    public Email getDefaultEmailByUserId(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        Email email=emailDao.getEmail(qIn);
        return email;
    }
}
