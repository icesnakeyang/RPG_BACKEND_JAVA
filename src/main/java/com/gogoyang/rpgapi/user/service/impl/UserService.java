package com.gogoyang.rpgapi.user.service.impl;

import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.job.dao.JobApplyLogDao;
import com.gogoyang.rpgapi.job.dao.JobMatchLogDao;
import com.gogoyang.rpgapi.job.entity.JobApplyLog;
import com.gogoyang.rpgapi.job.entity.JobMatchLog;
import com.gogoyang.rpgapi.user.dao.*;
import com.gogoyang.rpgapi.user.entity.*;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.user.vo.*;
import com.gogoyang.rpgapi.vo.Request;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.crypto.RsaMd5CksumType;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private final JobMatchLogDao jobMatchLogDao;
    private final JobApplyLogDao jobApplyLogDao;

    @Autowired
    public UserService(UserDao userDao, EmailDao emailDao, PhoneDao phoneDao, RealNameDao realNameDao, RoleUserDao roleUserDao, JobMatchLogDao jobMatchLogDao, JobApplyLogDao jobApplyLogDao) {
        this.userDao = userDao;
        this.emailDao = emailDao;
        this.phoneDao = phoneDao;
        this.realNameDao = realNameDao;
        this.roleUserDao = roleUserDao;
        this.jobMatchLogDao = jobMatchLogDao;
        this.jobApplyLogDao = jobApplyLogDao;
    }

    @Override
    @Transactional
    public Response createUser(CreateUserRequest request) {
        Response response = new Response();
        CreateUserResponse createUserResponse = new CreateUserResponse();
        User user=request.toUser();
        user.setRegisterTime(new Date());
        user.setToken(UUID.randomUUID().toString().replace("-",""));
        user.setUserRole(RoleType.NORMAL);
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
            user=buildUserInfoByToken(user.getToken());
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
        List<RoleUser> roleUser=roleUserDao.findByUserIdAndDisableTimeIsNull(user.getUserId());
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
        List<RoleUser> roleUsers=roleUserDao.findByUserIdAndDisableTimeIsNull(user.getUserId());
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
    @Transactional(rollbackOn = Exception.class)
    public void setSecretary(SetRoleRequest request) throws Exception {
        User user=userDao.findByUserId(request.getUserId());
        if(user==null){
            throw new Exception("no user exist");
        }
        user.setUserRole(request.getRole());
        userDao.save(user);
        List<RoleUser> roleUsers=roleUserDao.findByUserIdAndDisableTimeIsNull(user.getUserId());
        if(roleUsers.size()>0){
            for(int i=0;i<roleUsers.size();i++){
                if(roleUsers.get(i).getUserRole()==request.getRole()){
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
    @Transactional(rollbackOn = Exception.class)
    public Response SaveContactInfo(SvaeConfirmContactInfo info) throws Exception{
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

        user.setEmail(email.getEmail());
        user.setPhone(phone.getPhone());
        user.setRealName(realName.getRealName());
        userDao.save(user);

        Response response=new Response();
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Response saveProfile(SaveProfileRequest info) throws Exception{
        Response response=new Response();
        SvaeConfirmContactInfo contactInfo=new SvaeConfirmContactInfo();
        contactInfo.setToken(info.getToken());
        contactInfo.setEmail(info.getEmail());
        contactInfo.setPhone(info.getPhone());
        contactInfo.setRealName(info.getRealName());
        SaveContactInfo(contactInfo);
        return response;
    }

    public Page<User> loadUsers(UserPageRequest userPageRequest, RoleType roleType){
        Sort sort=new Sort(Sort.Direction.DESC, "userId");
        Pageable pageable=new PageRequest(userPageRequest.getPageIndex(),
                userPageRequest.getPageSize(), sort);
        Page<User> userPages=userDao.findByUserRole(roleType,pageable);

        return userPages;
    }

    public Page<User> loadUsersNotRole(UserPageRequest userPageRequest, RoleType roleTypeNot){
        Sort sort=new Sort(Sort.Direction.DESC, "userId");
        Pageable pageable=new PageRequest(userPageRequest.getPageIndex(),
                userPageRequest.getPageSize(), sort);
        Page<User> userPages=userDao.findByUserRoleNot(roleTypeNot,pageable);

        return userPages;
    }

    /**
     * load users that can be matched jobs
     * 读取可以被分配任务的user
     * @param request
     * @return
     */
    public ArrayList<User> loadUsersUnJob(Request request){
        /**
         * 1、查找jobApplyLog.result==null and jobApplyLog.jobId=request.jobId
         * 2、遍历jobApplyLog，查找每个applyUserId对应的user
         * 3、返回查到的user list
         *
         * 1. query jobApplyLog where result==null and jobApplyLog.jobId=request.jobId
         * 2. for each jobApplyLog, find out every user by the jobApplyLog.applyUserId
         * 3. return this user list.
         */
        List<JobApplyLog> jobApplyLogs=jobApplyLogDao.findAllByJobIdAndResultIsNull(request.getJobId());
        ArrayList<User> userList=new ArrayList<User>();
        for(int i=0;i<jobApplyLogs.size();i++){
            User user=userDao.findByUserId(jobApplyLogs.get(i).getApplyUserId());
            if(user!=null){
                userList.add(user);
            }
        }

        return userList;
    }
}