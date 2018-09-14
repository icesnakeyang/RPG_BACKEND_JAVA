package com.gogoyang.rpgapi.user.service.impl;

import com.gogoyang.rpgapi.user.dao.UserDao;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.user.vo.CreateUserRequest;
import com.gogoyang.rpgapi.user.vo.CreateUserResponse;
import com.gogoyang.rpgapi.user.vo.LoginRequest;
import com.gogoyang.rpgapi.user.vo.LoginResponse;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public Response createUser(CreateUserRequest request) {
        Response response = new Response();
        CreateUserResponse createUserResponse = new CreateUserResponse();
        User user=request.toUser();
        user.setRegisterTime(new Date());
        user.setToken(UUID.randomUUID().toString().replace("-",""));
        createUserResponse.setId(userDao.save(user).getUserId());
        response.setData(createUserResponse);
        return response;
    }

    @Override
    public Response buildUserById(Integer id) {
        Response response=new Response();
        User user=userDao.findOne(id);
        response.setData(user);
        return response;
    }

    @Override
    public Response loginUser(LoginRequest request) {
        Response response=new Response();
        LoginResponse loginResponse=new LoginResponse();
        User user=userDao.findByUsernameAndPassword(request.getUsername(),
                request.getPassword());
        if(user!=null){
            loginResponse.setUser(user);
        }
        response.setData(loginResponse);
        return response;
    }

    @Override
    public User buildUserByToken(String token) {
        User user=userDao.findByToken(token);

        return user;
    }
}