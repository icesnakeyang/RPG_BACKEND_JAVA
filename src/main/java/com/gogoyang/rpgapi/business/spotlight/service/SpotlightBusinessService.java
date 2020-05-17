package com.gogoyang.rpgapi.business.spotlight.service;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spotlight;
import com.gogoyang.rpgapi.meta.spotlight.service.ISpotlightService;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotlightBusinessService implements ISpotlightBusinessService{
    private final ISpotlightService iSpotlightService;

    @Autowired
    public SpotlightBusinessService(ISpotlightService iSpotlightService) {
        this.iSpotlightService = iSpotlightService;
    }

    @Override
    public Map listSpotlight(Map in) throws Exception {
        /**
         * 分页读取所有当前有效的申诉，用于申诉广场的列表显示
         * 把用户id的用户名加上
         * 统计下阅读人数
         */
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        Map qIn=new HashMap();
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Spotlight> spots=iSpotlightService.listSpotlight(qIn);

        Map out=new HashMap();
        out.put("spotlightList", spots);
        return out;
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
        Integer spotId=(Integer)in.get("spotId");

        Map qIn=new HashMap();
        qIn.put("spotlightId", spotId);
        Spotlight spot=iSpotlightService.getSpotlight(qIn);
        Map out=new HashMap();
        out.put("spot", spot);
        return out;
    }
}
