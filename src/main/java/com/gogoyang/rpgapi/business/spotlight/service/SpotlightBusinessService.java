package com.gogoyang.rpgapi.business.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpotlightBusinessService implements ISpotlightBusinessService{
    @Override
    public Map createSpotlight(Map in) throws Exception {
        /**
         * 1、根据jobId读取任务信息。无论任务处于什么状态，其实都可以申诉
         * 2、根据token获取当前用户。用户必须是甲方，或者乙方才能创建申诉
         * 3、如果任务当前已经有未撤销的申诉，则无论甲方或者乙方都不能再次发起申诉
         * 4、创建申诉
         * 5、创建申诉后，甲方和乙方都要扣除任务金额price对应的荣誉值honor
         * 6、更新userinfo里的honor
         */
        return null;
    }

    @Override
    public Page<Spot> listSpotlight(Map in) throws Exception {
        return null;
    }

    @Override
    public Map getSpotlightTiny(Map in) throws Exception {
        return null;
    }

    @Override
    public Map getSpotlightDetail(Map in) throws Exception {
        return null;
    }
}
