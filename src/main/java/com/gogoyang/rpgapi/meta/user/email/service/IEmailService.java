package com.gogoyang.rpgapi.meta.user.email.service;

import com.gogoyang.rpgapi.meta.user.email.entity.Email;

import java.util.ArrayList;

public interface IEmailService {
    Email insertEmail(Email email) throws Exception;

    ArrayList<Email> loadEmailByUserId(Integer userId) throws Exception;

    Email loadEmailByEmail(String email) throws Exception;

    void updateEmail(Email email) throws Exception;

}
