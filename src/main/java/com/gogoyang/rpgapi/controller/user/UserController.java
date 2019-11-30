package com.gogoyang.rpgapi.controller.user;

import com.gogoyang.rpgapi.business.common.ICommonBusinessService;
import com.gogoyang.rpgapi.business.user.IUserBusinessService;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserBusinessService iUserBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public UserController(IUserBusinessService iUserBusinessService,
                          ICommonBusinessService iCommonBusinessService) {
        this.iUserBusinessService = iUserBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 登录，loginName可以是email，或者phone
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public Response login(@RequestBody UserRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            in.put("loginName", request.getLoginName());
            in.put("password", request.getPassword());
            logMap.put("GogoActType", GogoActType.LOGIN);
            memoMap.put("loginName", request.getLoginName());
            Map out = iUserBusinessService.login(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10024);
                logger.error(ex.getMessage());
            }
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }

    /**
     * 手机号码注册
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/registerByPhone")
    public Response registerByPhone(@RequestBody UserRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            in.put("phone", request.getPhone());
            in.put("code", request.getCode());
            in.put("password", request.getPassword());
            in.put("realName", request.getRealName());
            logMap.put("GogoActType", GogoActType.REGISTER);
            memoMap.put("phone", request.getPhone());
            memoMap.put("realName", request.getRealName());
            Map out = iUserBusinessService.registerByPhone(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10014);
                logger.error(ex.getMessage());
            }
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
        }
        return response;
    }



    @ResponseBody
    @PostMapping("/getPhone")
    public Response getPhone(@RequestBody UserRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        Map out = new HashMap();
        try {
            in.put("phone", request.getPhone());
            out = iUserBusinessService.getPhone(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10124);
            }
        }
        return response;
    }
}
