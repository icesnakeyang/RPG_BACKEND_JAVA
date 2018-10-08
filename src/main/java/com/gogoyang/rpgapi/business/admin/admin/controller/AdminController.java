package com.gogoyang.rpgapi.business.admin.admin.controller;

import com.gogoyang.rpgapi.business.admin.vo.AdminRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final IRPGFunction irpgFunction;
    private final IAdminService iAdminService;

    @Autowired
    public AdminController(IRPGFunction irpgFunction, IAdminService iAdminService) {
        this.irpgFunction = irpgFunction;
        this.iAdminService = iAdminService;
    }

    /**
     * 创建一个根用户
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


            response.setData(rootAdmin);
        } catch (Exception ex) {
            response.setErrorMsg(ex.getMessage());
            return response;
        }

        return response;
    }

    /**
     * 创建一个Admin用户
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/create")
    Response createAdmin(@RequestBody AdminRequest request,
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
        String loginName = request.getLoginName();
        String password = request.getPassword();
        if (loginName == null) {
            response.setErrorCode(10017);
            return response;
        }

        try {
            Admin admin = iAdminService.loadAdminByLoginName(loginName);
            if (admin != null) {
                response.setErrorCode(10016);
                return response;
            }
            String token = httpServletRequest.getHeader("token");
            Admin superAdmin = iAdminService.loadAdminByToken(token);
            if (superAdmin == null) {
                response.setErrorCode(10004);
                return response;
            }
            if (!(superAdmin.getRoleType().ordinal() < request.getRoleType().ordinal())) {
                response.setErrorCode(10008);
                return response;
            }
            //enum less, authority more high
            Admin newAdmin = new Admin();
            newAdmin.setCreatedTime(new Date());
            newAdmin.setLoginName(request.getLoginName());
            newAdmin.setPassword(request.getPassword());
            newAdmin.setPassword(irpgFunction.encoderByMd5(newAdmin.getPassword()));
            newAdmin.setRoleType(request.getRoleType());
            newAdmin.setToken(UUID.randomUUID().toString().replace("-", ""));
            newAdmin = iAdminService.createAdmin(newAdmin);

            response.setData(newAdmin);

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
            Admin admin = iAdminService.loadAdminByLoginName(request.getLoginName());
            if (!irpgFunction.encoderByMd5(request.getPassword()).equals(admin.getPassword())) {
                response.setErrorCode(10024);
            }
            response.setData(admin);
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
            String token = httpServletRequest.getHeader("token");
            Admin admin = iAdminService.loadAdminByToken(token);
            if (admin == null) {
                response.setErrorCode(10004);
                return response;
            }
            ArrayList out = new ArrayList();

            if (admin.getRoleType().ordinal() < RoleType.SECRETARY.ordinal()) {
                ArrayList<Admin> list = iAdminService.loadAdminByRoleType(RoleType.SECRETARY);
                if (list.size() > 0) {
                    Map map=new HashMap();
                    map.put("role",RoleType.SECRETARY);
                    map.put("admin",list);
                    out.add(map);
                }
            }
            if (admin.getRoleType().ordinal() < RoleType.ADMINISTRATOR.ordinal()) {
                ArrayList<Admin> list = iAdminService.loadAdminByRoleType(RoleType.ADMINISTRATOR);
                if (list.size() > 0) {
                    Map map=new HashMap();
                    map.put("role",RoleType.ADMINISTRATOR);
                    map.put("admin",list);
                    out.add(map);
                }
            }
            if (admin.getRoleType().ordinal() < RoleType.SUPER_ADMIN.ordinal()) {
                ArrayList<Admin> list = iAdminService.loadAdminByRoleType(RoleType.SUPER_ADMIN);
                if (list.size() > 0) {
                    Map map=new HashMap();
                    map.put("role",RoleType.SUPER_ADMIN);
                    map.put("admin",list);
                    out.add(map);
                }
            }
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
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @GetMapping("/roleType")
    public Response loadRoleType(HttpServletRequest httpServletRequest){
        Response response=new Response();
        try{
            String token=httpServletRequest.getHeader("token");
            Admin admin=iAdminService.loadAdminByToken(token);
            if(admin==null){
                response.setErrorCode(10004);
                return response;
            }
            ArrayList roles=iAdminService.loadRoleTypes(admin.getRoleType());
            response.setData(roles);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10039);
                return response;
            }
        }
        return response;
    }
}
