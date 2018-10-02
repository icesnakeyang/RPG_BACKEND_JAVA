package com.gogoyang.rpgapi.user.realName.service;

import com.gogoyang.rpgapi.user.realName.entity.RealName;

import java.util.ArrayList;

public interface IRealNameService{
    RealName createName(RealName name);

    ArrayList<RealName> loadByUserId(Integer userId);
}
