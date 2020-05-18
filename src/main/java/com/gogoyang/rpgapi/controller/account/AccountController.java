package com.gogoyang.rpgapi.controller.account;

import com.gogoyang.rpgapi.business.account.IAccountBusinessService;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.vo.Response;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/account")
public class AccountController {
    private final IAccountBusinessService iAccountBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public AccountController(IAccountBusinessService iAccountBusinessService,
                             ICommonBusinessService iCommonBusinessService) {
        this.iAccountBusinessService = iAccountBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * Read my account list
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyAccount")
    public Response listMyAccount(@RequestBody AccountRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());

            logMap.put("GogoActType", GogoActType.LIST_ACCOUNT);
            logMap.put("token", token);
            Map out = iAccountBusinessService.listMyAccount(in);
            response.setData(out);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/loadAccountBalance")
    public Response getMyTotalAccount(@RequestBody AccountRequest request,
                                      HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            Map out = iAccountBusinessService.loadAccountBalance(in);
            response.setData(out);
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

    /**
     * 用户申请取现
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/withdraw")
    public Response withdraw(@RequestBody AccountRequest request,
                             HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("amount", request.getAmount());
            in.put("remark", request.getRemark());

            iAccountBusinessService.withdraw(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(20003);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/listWithdraw")
    public Response listWithdraw(@RequestBody AccountRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());

            Map out = iAccountBusinessService.listWithdraw(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(20005);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }


}
