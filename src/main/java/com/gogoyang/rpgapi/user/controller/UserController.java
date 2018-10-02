package com.gogoyang.rpgapi.user.controller;

import com.gogoyang.rpgapi.common.IRPGFunction;
import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.user.userInfo.service.IUserInfoService;
import com.gogoyang.rpgapi.user.vo.*;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 所有与User操作相关的API都在这里。
 * 操作User必须验证用户token，且部分方法需要ADMINISTRATOR权限
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final IRPGFunction irpgFunction;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public UserController(IRPGFunction irpgFunction, IUserInfoService iUserInfoService) {
        this.irpgFunction = irpgFunction;
        this.iUserInfoService = iUserInfoService;
    }

    /**
     * 注册一个用户
     * 无需验证token
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/register")
    public Response registerUser(@RequestBody UserRequest request) {
        Response response = new Response();

        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(request.getUsername());
            userInfo.setPassword(request.getPassword());

            userInfo = iUserInfoService.createUser(userInfo);
            response.setData(userInfo);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10014);
            }
        }
        return response;
    }

    /**
     * 根据userId读取一个用户
     *
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/{id}")
    public Response loadUserById(@PathVariable Integer id,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            UserInfo userInfo = iUserInfoService.checkToken(token);
            if(userInfo==null){
                response.setErrorCode(10004);
                return response;
            }
            UserInfo theUserInfo=iUserInfoService.loadUserByUserId(id);
            response.setData(theUserInfo);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }
        return response;
    }

    /**
     * 用户登录
     * 根据username, password
     * 返回 完整的 userInfo
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public Response loginUser(@RequestBody UserRequest request) {
        Response response = new Response();
        try {
            UserInfo userInfo = iUserInfoService.loadUserByUsername(request.getUsername());
            if (userInfo == null) {
                response.setErrorCode(10024);
                return response;
            }
            //verify the password
            if (!irpgFunction.encoderByMd5(request.getPassword()).equals(userInfo.getPassword())) {
                response.setErrorCode(10024);
                return response;
            }
            response.setData(userInfo);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10024);
            }
        }

        return response;
    }

    /**
     * 保存用户的email，phone，realname
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/saveContactInfo")
    public Response saveContactInfo(@RequestBody UserRequest request,
                                    HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            UserInfo userInfo=iUserInfoService.checkToken(token);
            if(userInfo==null){
                response.setErrorCode(10004);
                return response;
            }
            userInfo.setPhone(request.getPhone());
            userInfo.setRealName(request.getRealName());
            userInfo.setEmail(request.getEmail());
            iUserInfoService.saveContactInfo(userInfo);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10010);
                return response;
            }
        }

        return response;
    }

    /**
     * 根据用户上传的token，返回UserInfo
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @GetMapping("/userInfo")
    public Response loadUserInfo(HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            UserInfo userInfo=iUserInfoService.checkToken(token);
            if(userInfo==null){
                response.setErrorCode(10004);
                return response;
            }
            response.setData(userInfo);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(20028);
                return response;
            }
        }

        return response;
    }
}
