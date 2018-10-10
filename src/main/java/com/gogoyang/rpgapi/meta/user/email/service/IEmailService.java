package com.gogoyang.rpgapi.meta.user.email.service;

import com.gogoyang.rpgapi.meta.user.email.entity.Email;

import java.util.ArrayList;

public interface IEmailService {
    Email insertEmail(Email email) throws Exception;

    Email loadEmail(String email) throws Exception;

}
