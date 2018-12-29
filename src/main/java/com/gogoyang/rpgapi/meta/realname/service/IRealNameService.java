package com.gogoyang.rpgapi.meta.realname.service;

import com.gogoyang.rpgapi.meta.realname.entity.RealName;

public interface IRealNameService {
    void insert(RealName realName) throws Exception;
    void update(RealName realName) throws Exception;
    RealName getRealNameByUserId(Integer userId) throws Exception;
}
