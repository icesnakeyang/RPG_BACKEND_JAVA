package com.gogoyang.rpgapi.business.job.myJob.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spotlight;

import java.util.ArrayList;
import java.util.Map;

public interface IMySpotBusinessService {
    /**
     * 创建一个申诉
     * @param in
     * @return
     * @throws Exception
     */
    void createSpotlight(Map in) throws Exception;

    Map listMySpotlight(Map in) throws Exception;
}
