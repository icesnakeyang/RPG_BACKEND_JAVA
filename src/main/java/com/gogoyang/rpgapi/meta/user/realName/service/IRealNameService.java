package com.gogoyang.rpgapi.meta.user.realName.service;

import com.gogoyang.rpgapi.meta.user.realName.entity.RealName;

import java.util.ArrayList;

public interface IRealNameService{
    RealName createName(RealName name);

    ArrayList<RealName> loadByUserId(Integer userId);
}
