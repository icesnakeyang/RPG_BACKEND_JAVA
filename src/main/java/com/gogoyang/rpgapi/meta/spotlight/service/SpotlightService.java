package com.gogoyang.rpgapi.meta.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.dao.SpotlightDao;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spotlight;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class SpotlightService implements ISpotlightService {
    private  SpotlightDao spotlightDao;

    @Override
    public void insertSpotlight(Spotlight spotlight) throws Exception {
        if (spotlight.getSpotlightId() != null) {
            throw new Exception("10076");
        }
        spotlightDao.createSpotlight(spotlight);
    }

    /**
     * 批量查询申诉事件
     * @param qIn
     * jobId
     * status
     * offset
     * size
     * userId:甲方或者乙方
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Spotlight> listSpotlight(Map qIn) throws Exception {
        ArrayList<Spotlight> spotlights=spotlightDao.listSpotlight(qIn);
        return spotlights;
    }
}
