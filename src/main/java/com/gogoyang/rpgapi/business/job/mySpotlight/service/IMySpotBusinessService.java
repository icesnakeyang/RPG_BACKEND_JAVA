package com.gogoyang.rpgapi.business.job.mySpotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import org.springframework.data.domain.Page;

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

    Page<Spot> listMySpotlight(Map in) throws Exception;
}
