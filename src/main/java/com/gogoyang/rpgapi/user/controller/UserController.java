package com.gogoyang.rpgapi.user.controller;

import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.user.vo.CreateUserRequest;
import com.gogoyang.rpgapi.user.vo.LoginRequest;
import com.gogoyang.rpgapi.user.vo.SetRoleRequest;
import com.gogoyang.rpgapi.user.vo.SvaeConfirmContactInfo;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/create")
    public Response createUser(@RequestBody CreateUserRequest request){
        Response response= userService.createUser(request);
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());
        return response=userService.loginUser(loginRequest);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Response buildUser(@PathVariable Integer id){
        Response response=new Response();
        response.setData(userService.buildUserById(id));
        return response;
    }

    @ResponseBody
    @PostMapping("/login")
    public Response loginUser(@RequestBody LoginRequest request){
        return userService.loginUser(request);
    }

    @ResponseBody
    @PostMapping("/saveContactInfo")
    public Response ConfirmContactInfo(@RequestBody SvaeConfirmContactInfo info,
                                       HttpServletRequest httpServletRequest){
        String token=httpServletRequest.getHeader("token");
        info.setToken(token);
        return userService.SaveContactInfo(info);
    }

    @ResponseBody
    @PostMapping("/profile")
    public Response LoadUserInfo(HttpServletRequest httpServletRequest){
        String token=httpServletRequest.getHeader("token");
        Response response=new Response();
        response.setData(userService.buildUserInfoByToken(token));
        return response;
    }

    @ResponseBody
    @PostMapping("/setAdmin")
    public Response SetAdmin(@RequestBody SetRoleRequest setRoleRequest,
                             HttpServletRequest httpServletRequest){
        /**
         * token is the operator user
         * userId is the user to set
         */
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(token==null){
            //there is no token in header
            response.setErrorCode(10004);
            return response;
        }
        User user=userService.buildUserInfoByToken(token);
        if(user==null){
            //the token is not correct
            response.setErrorCode(10004);
            return response;
        }
        if(user.getUserRole()!=RoleType.ROOT_ADMIN){
            if(user.getUserRole()!=RoleType.SUPER_ADMIN){
                //the user do not has authority to set admin user
                response.setErrorCode(10008);
                return response;
            }
        }
        setRoleRequest.setOperatorId(user.getUserId());
        try {
            userService.setAdmin(setRoleRequest);
        }catch (Exception ex){
            response.setErrorCode(10009);
            return response;
        }
        return response;
    }
}
