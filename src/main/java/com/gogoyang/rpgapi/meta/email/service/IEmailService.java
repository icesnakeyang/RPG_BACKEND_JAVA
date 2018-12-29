package com.gogoyang.rpgapi.meta.email.service;

import com.gogoyang.rpgapi.meta.email.entity.Email;

public interface IEmailService {
    Email getEmailByEmail(String emailName) throws Exception;
}
