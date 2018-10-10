package com.gogoyang.rpgapi.meta.user.realName.service;

import com.gogoyang.rpgapi.meta.user.realName.entity.RealName;

import java.util.ArrayList;

public interface IRealNameService{
    RealName insertRealName(RealName name) throws Exception;

    ArrayList<RealName> loadRealNameByUserId(Integer userId) throws Exception;
}
