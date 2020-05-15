package com.gogoyang.rpgapi.meta.spotlight.dao;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spotlight;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;


@Mapper
public interface SpotlightDao {
    void createSpotlight(Spotlight spotlight);

    /**
     * getSpotlight
     * @param qIn
     * spotlightId
     * jobId
     * status
     * @return
     */
    Spotlight getSpotlight(Map qIn);

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
    ArrayList<Spotlight> listSpotlight(Map qIn);
}
