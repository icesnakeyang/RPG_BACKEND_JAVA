package com.gogoyang.rpgapi.business.common;

import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class RPGService implements IRPGService {

    /**
     * 读取所有RoleType里的枚举值
     *
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList loadRoleTypes() throws Exception {
        ArrayList roles = new ArrayList();
        for (RoleType s : RoleType.values()) {
            Map map = new HashMap();
            map.put("value", s.ordinal());
            map.put("label", s);
            roles.add(map);
        }
        return roles;
    }
}
