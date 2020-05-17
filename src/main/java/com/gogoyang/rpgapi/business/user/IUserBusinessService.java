package com.gogoyang.rpgapi.business.user;

import java.util.Map;

public interface IUserBusinessService {
    Map login(Map in) throws Exception;

    Map registerByEmail(Map in) throws Exception;

    Map registerByPhone(Map in) throws Exception;

    Map getPhone(Map in) throws Exception;
}
