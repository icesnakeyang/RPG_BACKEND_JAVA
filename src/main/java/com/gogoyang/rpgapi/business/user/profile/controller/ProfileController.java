package com.gogoyang.rpgapi.business.user.profile.controller;

import com.gogoyang.rpgapi.business.user.profile.service.IProfileBusinessService;
import com.gogoyang.rpgapi.business.user.profile.vo.ProfileRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/profile")
public class ProfileController {
    private final IProfileBusinessService iProfileBusinessService;

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
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10048);
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
}
