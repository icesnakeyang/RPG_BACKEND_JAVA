package com.gogoyang.rpgapi.user.service.impl;

import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.user.dao.*;
import com.gogoyang.rpgapi.user.entity.*;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.user.vo.*;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final UserDao userDao;
    private final EmailDao emailDao;
    private final PhoneDao phoneDao;
    private final RealNameDao realNameDao;
    private final RoleUserDao roleUserDao;

    @Autowired
    public UserService(UserDao userDao, EmailDao emailDao, PhoneDao phoneDao, RealNameDao realNameDao, RoleUserDao roleUserDao) {
        this.userDao = userDao;
        this.emailDao = emailDao;
        this.phoneDao = phoneDao;
        this.realNameDao = realNameDao;
        this.roleUserDao = roleUserDao;
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
        List<RoleUser> roleUser=roleUserDao.findByUserIdAndDisableIsNull(user.getUserId());
        if(roleUser.size()==1){
            user.setUserRole(roleUser.get(0).getUserRole());
        }
        return user;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setAdmin(SetRoleRequest request) throws Exception{
        /**
         * 1、读取要设置的user，查看是否存在
         * 2、设置user的role，保存
         * 3、读取roleuser，如果存在，修改为disable，保存
         * 4、保存新的roleuser
         */
        User user=userDao.findByUserId(request.getUserId());
        if(user==null){
            throw new Exception("no user exist");
        }
        user.setUserRole(request.getRole());
        userDao.save(user);
        List<RoleUser> roleUsers=roleUserDao.findByUserIdAndDisableIsNull(user.getUserId());
        if(roleUsers.size()>0){
            for(int i=0;i<roleUsers.size();i++){
                if(roleUsers.get(i).getUserRole()==request.getRole()){
                    //no need to set same permission
                    return;
                }
            }
        }
        RoleUser roleUser=new RoleUser();
        roleUser.setCreatedTime(new Date());
        roleUser.setCreatedUserId(request.getOperatorId());
        roleUser.setUserId(request.getUserId());
        roleUser.setUserRole(request.getRole());
        roleUserDao.save(roleUser);
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