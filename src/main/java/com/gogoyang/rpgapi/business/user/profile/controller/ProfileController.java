package com.gogoyang.rpgapi.business.user.profile.controller;

import com.gogoyang.rpgapi.business.user.profile.service.IProfileBusinessService;
import com.gogoyang.rpgapi.business.user.profile.vo.ProfileRequest;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/user/profile")
public class ProfileController {
    private final IProfileBusinessService iProfileBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    public ProfileController(IProfileBusinessService iProfileBusinessService) {
        this.iProfileBusinessService = iProfileBusinessService;
    }

    @ResponseBody
    @PostMapping("/getUserInfo")
    public Response getUserInfo(@RequestBody ProfileRequest request,
                                HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            Map out=iProfileBusinessService.getUserInfo(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10028);
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
    public Response saveContactInfo(@RequestBody ProfileRequest request,
                                    HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("email", request.getEmail());
            in.put("phone", request.getPhone());
            in.put("realName", request.getRealName());
            iProfileBusinessService.saveUserContactInfo(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/listPhoneOfUser")
    public Response listPhoneOfUser(@RequestBody ProfileRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            Map out=iProfileBusinessService.listPhoneOfUser(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10124);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/listEmailOfUser")
    public Response listEmailOfUser(@RequestBody ProfileRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            Map out=iProfileBusinessService.listEmailOfUser(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10124);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getUserProfile")
    public Response getUserProfile(@RequestBody ProfileRequest request,
                                   HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            Map out=iProfileBusinessService.getUserProfile(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10125);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/saveRealName")
    public Response saveRealName(@RequestBody ProfileRequest request,
                                 HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("realname", request.getRealName());
            in.put("idcardNo", request.getIdcardNo());
            in.put("sex", request.getSex());
            iProfileBusinessService.saveRealName(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10126);
            }
        }
        return response;
    }
}
