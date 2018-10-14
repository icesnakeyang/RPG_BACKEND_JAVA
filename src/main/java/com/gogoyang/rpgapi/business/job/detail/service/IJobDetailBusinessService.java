package com.gogoyang.rpgapi.business.job.detail.service;

import java.util.Map;

public interface IJobDetailBusinessService {
    Map loadJobDetail(Map in) throws Exception;

    Map loadJobDetailLogin(Map in) throws Exception;
}
