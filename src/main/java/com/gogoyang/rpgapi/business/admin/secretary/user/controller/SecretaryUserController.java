package com.gogoyang.rpgapi.business.admin.secretary.user.controller;

import com.gogoyang.rpgapi.business.admin.secretary.user.service.ISecretaryUserBusinessService;
import com.gogoyang.rpgapi.business.admin.vo.AdminRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/secretary/user")
public class SecretaryUserController {
    private final ISecretaryUserBusinessService iSecretaryUserBusinessService;

    @Autowired
    public SecretaryUserController(ISecretaryUserBusinessService iSecretaryUserBusinessService) {
        this.iSecretaryUserBusinessService = iSecretaryUserBusinessService;
    }

    /**
     * read history of user apply
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listApplyHistory")
    public Response listApplyHistory(@RequestBody AdminRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        try{
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("userId", request.getUserId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iSecretaryUserBusinessService.listApplyHistory(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10108);
            }
        }
        return response;
    }

}
