package com.gogoyang.rpgapi.business.common;

import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonBusinessService implements ICommonBusinessService {
    private final IUserService iUserService;

    public CommonBusinessService(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @Override
    public User getUserByToken(String token) throws Exception {
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            //读取用户信息失败
            throw new Exception("10028");
        }
        return user;
    }
}
