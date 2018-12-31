package com.gogoyang.rpgapi.business.user.register.service;


import java.util.Map;

public interface IUserRegisterBusinessService {
    Map registerByEmail(Map in) throws Exception;

    Map getEmailByEmail(Map in) throws Exception;
}
