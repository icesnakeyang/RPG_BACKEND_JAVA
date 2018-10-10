package com.gogoyang.rpgapi.meta.user.email.service;

import com.gogoyang.rpgapi.meta.user.email.dao.EmailDao;
import com.gogoyang.rpgapi.meta.user.email.entity.Email;
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


    /**
     * 创建一个email，返回带id的email
     * @param email
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Email insertEmail(Email email) throws Exception {
        if(email.getEmailId()!=null){
            throw new Exception("10010");
        }
        email=emailDao.save(email);
        return email;
    }

    /**
     * 读取一个用户的所有email
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Email> loadEmailByUserId(Integer userId) throws Exception {
        ArrayList<Email> emails=emailDao.findAllByUserId(userId);
        return emails;
    }

    /**
     * 从数据库读取一个email
     * @param email
     * @return
     * @throws Exception
     */
    @Override
    public Email loadEmailByEmail(String email) throws Exception {
        Email theEmail=emailDao.findByEmail(email);
        return theEmail;
    }

    /**
     * 修改一个email
     * @param email
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateEmail(Email email) throws Exception {
        if(email.getEmailId()==null){
            throw new Exception("10010");
        }
        emailDao.save(email);
    }
}
