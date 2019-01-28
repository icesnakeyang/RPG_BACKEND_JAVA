package com.gogoyang.rpgapi.business.account.controller;

import com.gogoyang.rpgapi.business.account.service.IAccountBusinessService;
import com.gogoyang.rpgapi.business.account.vo.AccountRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final IAccountBusinessService iAccountBusinessService;

    @Autowired
    public AccountController(IAccountBusinessService iAccountBusinessService) {
        this.iAccountBusinessService = iAccountBusinessService;
    }

    /**
     * Read my account list
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyAccount")
    public Response listMyAccount(@RequestBody AccountRequest request,
                                  HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Page<Account> accountList=iAccountBusinessService.listMyAccount(in);
            response.setData(accountList);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10106);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/loadAccountBalance")
    public Response getMyTotalAccount(@RequestBody AccountRequest request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            Map out=iAccountBusinessService.loadAccountBalance(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10106);
            }
        }
        return response;
    }
}
