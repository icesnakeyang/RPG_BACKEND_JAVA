package com.gogoyang.rpgapi.business.job.mySpotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;

import java.util.ArrayList;
import java.util.Map;

public interface IMySpotBusinessService {
    /**
     * 创建一个申诉
     * @param in
     * @return
     * @throws Exception
     */
    Map createSpotlight(Map in) throws Exception;

    ArrayList<Spot> listMySpotlight(Map in) throws Exception;
}
