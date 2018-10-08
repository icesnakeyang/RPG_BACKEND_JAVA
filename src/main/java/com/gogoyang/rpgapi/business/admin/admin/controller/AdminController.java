package com.gogoyang.rpgapi.business.admin.admin.controller;

import com.gogoyang.rpgapi.business.admin.admin.service.IAdminBusinessService;
import com.gogoyang.rpgapi.business.admin.vo.AdminRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final IRPGFunction irpgFunction;
    private final IAdminBusinessService iAdminBusinessService;

    @Autowired
    public AdminController(IRPGFunction irpgFunction, IAdminService iAdminService, IAdminBusinessService iAdminBusinessService) {
        this.irpgFunction = irpgFunction;
        this.iAdminService = iAdminService;
        this.iAdminBusinessService = iAdminBusinessService;
    }

    /**
     * 创建一个根用户
     * create root admin user
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/create/root")
    Response createRoot(@RequestBody AdminRequest request,
                        HttpServletRequest httpServletRequest) {

        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            token = irpgFunction.encoderByMd5(token);
            if (!token.equals("NnGCAFR5YVztS4DjtoIdpg==")) {
                response.setErrorMsg("Don't call me Daddy");
                return response;
            }
            Map in = new HashMap();
            in.put("loginName", request.getLoginName());
            in.put("password", request.getPassword());
            in.put("roleType", RoleType.ROOT_ADMIN);
            Map out = iAdminBusinessService.createRootAdmin(in);
            response.setData(out);
        } catch (Exception ex) {
            response.setErrorMsg(ex.getMessage());
            return response;
        }
        return response;
    }

    /**
     * 创建一个superAdmin用户
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/create/super")
    Response createSuperAdmin(@RequestBody AdminRequest request,
                         HttpServletRequest httpServletRequest) {

        Response response = new Response();

        /**
         * 1. if request.loginName=="" or null, set Response.errorCode=10017, return
         * 2. check loginName is duplicate or not, if duplicate set Response.errorCode=10016, return
         *      iAdminService.loadAdminByLoginName(loginName)
         * 3. read token from header
         * 4. iRPGFunction.checkToken, set response.errorCode= 10004 if false, return
         * 3. read Admin by token.
         * 4. check admin.roleType
         *      ROOT_ADMIN can create SUPER_ADMIN
         *      SUPER_ADMIN can create ADMINISTRATOR
         *      ADMINISTRATOR can create SECRETARY
         *      if error set Response.errorCode = 10008
         * 5. define newAdmin object. set every property.
         *      use irpgFunction.encoderByMd5 to encode the password
         *      use UUID to generate a token for the new admin
         *      set createdTime and createdUserId=admin.adminId
         * 6. iAdminService.createAdmin to create the admin.
         * 7. set response.data=admin
         *
         */
        try {
            String loginName = request.getLoginName();
            String password = request.getPassword();
            Map in = new HashMap();
            Map out = iAdminBusinessService.createSuperAdmin(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode((Integer.parseInt(ex.getMessage())));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10017);
                return response;
            }
        }
        return response;
    }

    /**
     * 创建一个Administrator用户
     * create an Administrator
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/create/administrator")
    Response createAdministrator(@RequestBody AdminRequest request,
                                 HttpServletRequest httpServletRequest){
        Response response=new Response();
        try{
            Map in=new HashMap();
            in.put("loginName", request.getLoginName());
            in.put("password", request.getPassword());
            Map out=new HashMap();
            out=iAdminBusinessService.createAdministrator(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10017);
            }
        }
        return response;
    }

    /**
     * Admin登录
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    Response loginAdmin(@RequestBody AdminRequest request) {

        Response response = new Response();
        /**
         * read admin by request.loginName and password.
         * set response.data=admin
         */
        try {
            Map in=new HashMap();
            in.put("loginName", request.getLoginName());
            in.put("password", request.getPassword());
            Map out=iAdminBusinessService.login(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10025);
            }
        }

        return response;
    }

    /**
     * 读取所有当前用户权限可以查看的管理员用户，包括RPG秘书
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @GetMapping("/admin")
    Response loadAdmins(HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in=new HashMap();
            in.put("token", httpServletRequest.getHeader("token"));
            Map out=iAdminBusinessService.loadAdmin(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10038);
            }
        }
        return response;
    }

    /**
     * 读取所有用户权限的值
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @GetMapping("/roleType")
    public Response loadRoleType() {
        Response response = new Response();
        try {
            ArrayList roles = iAdminService.loadRoleTypes(admin.getRoleType());
            response.setData(roles);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10039);
                return response;
            }
        }
        return response;
    }
}
