package com.gogoyang.rpgapi.user.service;

import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.vo.*;
import com.gogoyang.rpgapi.vo.Request;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

public interface IUserService {
    Response createUser(CreateUserRequest request);

    User buildUserById(Integer id);

    Response loginUser(LoginRequest request);

    User buildUserByToken(String token);

    Response SaveContactInfo(SvaeConfirmContactInfo info) throws Exception;

    Response saveProfile(SaveProfileRequest info) throws Exception;

    User buildUserInfoByToken(String token);

    void setAdmin(SetRoleRequest request) throws Exception;

    void setSecretary(SetRoleRequest request) throws Exception;

    Page<User> loadUsers(UserPageRequest userPageRequest, RoleType roleType);

    Page<User> loadUsersNotRole(UserPageRequest userPageRequest, RoleType roleTypeNot);

    ArrayList<User> loadUsersUnJob(Request request);
}
