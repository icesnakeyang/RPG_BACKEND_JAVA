package com.gogoyang.rpgapi.user.service.impl;

import com.gogoyang.rpgapi.user.dao.EmailDao;
import com.gogoyang.rpgapi.user.dao.PhoneDao;
import com.gogoyang.rpgapi.user.dao.RealNameDao;
import com.gogoyang.rpgapi.user.dao.UserDao;
import com.gogoyang.rpgapi.user.entity.Email;
import com.gogoyang.rpgapi.user.entity.Phone;
import com.gogoyang.rpgapi.user.entity.RealName;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.user.vo.*;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final UserDao userDao;
    private final EmailDao emailDao;
    private final PhoneDao phoneDao;
    private final RealNameDao realNameDao;

    @Autowired
    public UserService(UserDao userDao, EmailDao emailDao, PhoneDao phoneDao, RealNameDao realNameDao) {
        this.userDao = userDao;
        this.emailDao = emailDao;
        this.phoneDao = phoneDao;
        this.realNameDao = realNameDao;
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
    public User buildUserById(Integer id) {
        Response response=new Response();
        User user=userDao.findOne(id);
        return user;
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

    @Override
    public User buildUserInfoByToken(String token){
        User user=userDao.findByToken(token);
        Email email=emailDao.findByUserId(user.getUserId());
        if(email!=null) {
            user.setEmail(email.getEmail());
        }
        Phone phone=phoneDao.findByUserId(user.getUserId());
        if(phone!=null) {
            user.setPhone(phone.getPhone());
        }
        RealName realName=realNameDao.findByUserId(user.getUserId());
        if(realName!=null) {
            user.setRealName(realName.getRealName());
        }
        return user;
    }

    @Override
    @Transactional
    public Response SaveContactInfo(SvaeConfirmContactInfo info) {
        User user=userDao.findByToken(info.getToken());

        Email email=emailDao.findByUserId(user.getUserId());
        if(email==null){
            email=new Email();
        }
        email.setCreatedTime(new Date());
        email.setEmail(info.getEmail());
        email.setUserId(user.getUserId());
        emailDao.save(email);

        Phone phone=phoneDao.findByUserId(user.getUserId());
        if(phone==null) {
            phone = new Phone();
        }
        phone.setCreatedTime(new Date());
        phone.setPhone(info.getPhone());
        phone.setUserId(user.getUserId());
        phoneDao.save(phone);

        RealName realName=realNameDao.findByUserId(user.getUserId());
        if(realName==null) {
            realName=new RealName();
        }
        realName.setRealName(info.getRealName());
        realName.setUserId(user.getUserId());
        realNameDao.save(realName);

        Response response=new Response();
        return response;
    }
}