package com.gogoyang.rpgapi.controller.account;

import com.gogoyang.rpgapi.business.account.IAccountBusinessService;
import com.gogoyang.rpgapi.framework.vo.Response;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/account")
public class AccountController {
    private final IAccountBusinessService iAccountBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

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
                logger.error(ex.getMessage());
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
                logger.error(ex.getMessage());
            }
        }
        return response;
    }

    /**
     * 用户申请取现
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/withdraw")
    public Response withdraw(@RequestBody AccountRequest request,
                             HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        try{
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("userId", request.getUserId());
            in.put("amount", request.getAmount());
            in.put("remark", request.getRemark());

            iAccountBusinessService.withdraw(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(20003);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }
}
