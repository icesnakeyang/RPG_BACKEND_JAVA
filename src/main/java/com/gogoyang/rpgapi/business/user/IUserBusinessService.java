package com.gogoyang.rpgapi.business.user;

import java.util.Map;

public interface IUserBusinessService {
    Map login(Map in) throws Exception;

    Map register(Map in) throws Exception;
}
