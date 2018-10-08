package com.gogoyang.rpgapi.meta.user.email.service;

import com.gogoyang.rpgapi.meta.user.email.dao.EmailDao;
import com.gogoyang.rpgapi.meta.user.email.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService{
    private final EmailDao emailDao;

    @Autowired
    public EmailService(EmailDao emailDao) {
        this.emailDao = emailDao;
    }


    /**
     * 创建一个email，返回带ids的email
     * @param email
     * @return
     * @throws Exception
     */
    @Override
    public Email createEmail(Email email) throws Exception {
        email=emailDao.save(email);
        return email;
    }
}
