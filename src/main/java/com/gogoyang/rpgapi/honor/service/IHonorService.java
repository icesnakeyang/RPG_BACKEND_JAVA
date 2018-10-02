package com.gogoyang.rpgapi.honor.service;

import com.gogoyang.rpgapi.honor.entity.Honor;


public interface IHonorService {
    void deductHonor(Honor honor) throws Exception;

}
