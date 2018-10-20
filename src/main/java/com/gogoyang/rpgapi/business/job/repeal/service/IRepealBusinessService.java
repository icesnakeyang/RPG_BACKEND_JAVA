package com.gogoyang.rpgapi.business.job.repeal.service;

import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;

import java.util.Map;

public interface IRepealBusinessService {
    Repeal createRepeal(Map in) throws Exception;
}
