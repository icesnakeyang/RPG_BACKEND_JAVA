package com.gogoyang.rpgapi.meta.email.service;

import com.gogoyang.rpgapi.meta.email.entity.Email;

import java.util.ArrayList;

public interface IEmailService {
    Email getEmailByEmail(String emailName) throws Exception;

    void insert(Email email) throws Exception;

    ArrayList<Email> listEmailByUserId(Integer userId) throws Exception;

    void updateEmail(Email email) throws Exception;

    Email getDefaultEmailByUserId(Integer userId) throws Exception;
}
