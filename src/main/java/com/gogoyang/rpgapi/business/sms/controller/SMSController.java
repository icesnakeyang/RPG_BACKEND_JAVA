package com.gogoyang.rpgapi.business.sms.controller;

import com.gogoyang.rpgapi.business.admin.vo.AdminRequest;
import com.gogoyang.rpgapi.business.sms.business.ISMSBusinessService;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SMSController {
    private final ISMSBusinessService ismsBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    public SMSController(ISMSBusinessService ismsBusinessService) {
        this.ismsBusinessService = ismsBusinessService;
    }

    @ResponseBody
    @PostMapping("/getPhoneVerifyCode")
    public Response getPhoneVerifyCode(@RequestBody AdminRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            in.put("phone", request.getPhone());
            ismsBusinessService.getPhoneVerifyCode(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10106);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }
}
