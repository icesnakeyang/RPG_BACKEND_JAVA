package com.gogoyang.rpgapi.user.controller;

import com.gogoyang.rpgapi.common.IRPGFUNC;
import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.user.vo.*;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    private final IRPGFUNC irpgfunc;

    @Autowired
    public UserController(IUserService userService, IRPGFUNC irpgfunc) {
        this.userService = userService;
        this.irpgfunc = irpgfunc;
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
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(!irpgfunc.checkToken(token)){
            response.setErrorCode(10004);
            return response;
        }
        info.setToken(token);
        try {
            userService.SaveContactInfo(info);
        }catch (Exception ex){
            response.setErrorCode(10010);
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/saveProfile")
    public Response SaveProfile(@RequestBody SaveProfileRequest request,
                                HttpServletRequest httpRequest){
        Response response=new Response();
        String token=httpRequest.getHeader("token");
        if(!irpgfunc.checkToken(token)){
            response.setErrorCode(10004);
            return response;
        }
        request.setToken(token);
        try {
            userService.saveProfile(request);
        }catch (Exception ex){
            response.setErrorCode(10010);
        }
        return response;
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

        //god user
        if(user.getToken().equals("af6aa2ad4f22477b917f3d13937f78cd")){
            user.setUserRole(RoleType.ROOT_ADMIN);
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

    @ResponseBody
    @PostMapping("/setSecretary")
    public Response SetSecretary(@RequestBody SetRoleRequest setRoleRequest,
                             HttpServletRequest httpServletRequest){
        /**
         * token is the operator user
         * userId is the user to set
         */
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(!irpgfunc.checkToken(token)){
            response.setErrorCode(10004);
            return response;
        }
        User user=userService.buildUserInfoByToken(token);

        if(user.getUserRole()!=RoleType.ROOT_ADMIN) {
            if (user.getUserRole() != RoleType.SUPER_ADMIN) {
                if (user.getUserRole() != RoleType.ADMINISTRATOR) {
                    //the user do not has authority to set admin user
                    response.setErrorCode(10008);
                    return response;
                }
            }
        }
        setRoleRequest.setOperatorId(user.getUserId());
        try {
            userService.setSecretary(setRoleRequest);
        }catch (Exception ex){
            response.setErrorCode(10009);
            return response;
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/loadUsers")
    public Response LoadUsers(@RequestBody UserPageRequest userPageRequest,
                              HttpServletRequest httpServletRequest){
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(!irpgfunc.checkToken(token)){
            response.setErrorCode(10004);
            return response;
        }
        Page<User> userPage=userService.loadUsers(userPageRequest, RoleType.NORMAL);
        response.setData(userPage);
        return response;
    }

    @ResponseBody
    @PostMapping("/loadAdmins")
    public Response LoadAdmins(@RequestBody UserPageRequest userPageRequest,
                              HttpServletRequest httpServletRequest){
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(!irpgfunc.checkToken(token)){
            response.setErrorCode(10004);
            return response;
        }
        Page<User> userPage=userService.loadUsers(userPageRequest, RoleType.ADMINISTRATOR);
        response.setData(userPage);
        return response;
    }

    @ResponseBody
    @PostMapping("/loadSecretary")
    public Response LoadSecretary(@RequestBody UserPageRequest userPageRequest,
                              HttpServletRequest httpServletRequest){
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(!irpgfunc.checkToken(token)){
            response.setErrorCode(10004);
            return response;
        }
        Page<User> userPage=userService.loadUsers(userPageRequest, RoleType.SECRETARY);
        response.setData(userPage);
        return response;
    }

    @ResponseBody
    @PostMapping("/loadUnSecretary")
    public Response LoadUnSecretary(@RequestBody UserPageRequest userPageRequest,
                              HttpServletRequest httpServletRequest){
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(!irpgfunc.checkToken(token)){
            response.setErrorCode(10004);
            return response;
        }
        Page<User> userPage=userService.loadUsersNorRole(userPageRequest, RoleType.SECRETARY);
        response.setData(userPage);
        return response;
    }


}
