package com.gogoyang.rpgapi.user.service;

import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.vo.CreateUserRequest;
import com.gogoyang.rpgapi.user.vo.LoginRequest;
import com.gogoyang.rpgapi.vo.Response;

public interface IUserService {
    Response createUser(CreateUserRequest request);

    Response buildUserById(Integer id);

    Response loginUser(LoginRequest request);

    User buildUserByToken(String token);
}
