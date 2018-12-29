package com.gogoyang.rpgapi.meta.email.service;

import com.gogoyang.rpgapi.meta.email.dao.EmailDao;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
