package com.gogoyang.rpgapi.meta.spotlight.service;


import com.gogoyang.rpgapi.meta.spotlight.entity.Spotlight;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public interface ISpotlightService {
    /**
     * 创建一个申诉
     * @param spot
     * @throws Exception
     */
    void insertSpotlight(Spotlight spot) throws Exception;

    /**
     * getSpotlight
     * @param qIn
     * spotlightId
     * jobId
     * status
     * @return
     * @throws Exception
     */
    Spotlight getSpotlight(Map qIn) throws Exception;

    /**
     * 批量查询申诉事件
     * @param qIn
     * jobId
     * status
     * offset
     * size
     * userId:甲方或者乙方
     * @return
     */
    ArrayList<Spotlight> listSpotlight(Map qIn) throws Exception;

}
