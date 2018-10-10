package com.gogoyang.rpgapi.meta.honor.service;


import com.gogoyang.rpgapi.meta.honor.entity.Honor;

public interface IHonorService {
    void deductHonor(Honor honor) throws Exception;

}
