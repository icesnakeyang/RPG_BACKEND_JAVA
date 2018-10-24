package com.gogoyang.rpgapi.business.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpotlightBusinessService implements ISpotlightBusinessService{

    @Override
    public Page<Spot> listSpotlight(Map in) throws Exception {
        /**
         * 分页读取所有当前有效的申诉，用于申诉广场的列表显示
         * 把用户id的用户名加上
         * 统计下阅读人数
         */
        return null;
    }

    @Override
    public Map getSpotlightTiny(Map in) throws Exception {
        /**
         * 根据jobId读取一个简要的申诉信息，不包含详情
         */
        return null;
    }

    @Override
    public Map getSpotlightDetail(Map in) throws Exception {
        /**
         * 根据jobId读取详细的申诉，包括详情，用于申诉详情页显示
         * 根据token获取当前用户，如果不是创建用户，就增加一次views。
         * views并不判断一个用户的重复访问，只要访问一次就加一次
         */
        return null;
    }
}
