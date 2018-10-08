package com.gogoyang.rpgapi.topUp.service;

import com.gogoyang.rpgapi.topUp.dao.TopUpDao;
import com.gogoyang.rpgapi.topUp.entity.TopUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TopUpService implements ITopUpService{
    private final TopUpDao topUpDao;

    @Autowired
    public TopUpService(TopUpDao topUpDao) {
        this.topUpDao = topUpDao;
    }

    @Override
    public TopUp insertTopUp(TopUp topUp) throws Exception {
        return null;
    }

    @Override
    public ArrayList<TopUp> loadTopUpApply() throws Exception {
        return null;
    }

    @Override
    public ArrayList<TopUp> loadTopUpHistory(Integer userId) throws Exception {
        return null;
    }
}
