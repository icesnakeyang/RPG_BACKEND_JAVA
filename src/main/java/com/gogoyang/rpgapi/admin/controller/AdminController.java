package com.gogoyang.rpgapi.admin.controller;

import com.gogoyang.rpgapi.admin.entity.Admin;
import com.gogoyang.rpgapi.admin.service.IAdminService;
import com.gogoyang.rpgapi.admin.vo.AdminRequest;
import com.gogoyang.rpgapi.common.IRPGFunction;
import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.job.ENTITY.job.Job;
import com.gogoyang.rpgapi.job.job.service.IJobService;
import com.gogoyang.rpgapi.job.jobMatch.entity.JobMatch;
import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.user.userInfo.service.IUserInfoService;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final IAdminService iAdminService;
    private final IJobService iJobService;
    private final IRPGFunction irpgFunction;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public AdminController(IAdminService iAdminService, IJobService iJobService, IRPGFunction irpgFunction, IUserInfoService iUserInfoService) {
        this.iAdminService = iAdminService;
        this.iJobService = iJobService;
        this.irpgFunction = irpgFunction;
        this.iUserInfoService = iUserInfoService;
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
            Admin rootAdmin = new Admin();
            rootAdmin.setLoginName(request.getLoginName());
            rootAdmin.setPassword(request.getPassword());
            rootAdmin.setCreatedTime(new Date());
            rootAdmin.setPassword(irpgFunction.encoderByMd5(rootAdmin.getPassword()));
            rootAdmin.setRoleType(request.getRoleType());
            rootAdmin.setToken(UUID.randomUUID().toString().replace("-", ""));
            rootAdmin = iAdminService.createAdmin(rootAdmin);

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
     * 获取所有需要匹配的任务
     * rpg秘书查看用户提交的任务申请，选择用户分配。
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/loadJobToMatch")
    public Response loadJobToMatch(@RequestBody AdminRequest request,
                                   HttpServletRequest httpServletRequest) {
        /**
         * 检查admin是否存在
         * 检查admin是否有Secretary权限
         * 读取所有用户申请了任务
         */
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Admin admin = iAdminService.loadAdminByToken(token);
            if (admin == null) {
                response.setErrorCode(10004);
                return response;
            }

            if (admin.getRoleType() != RoleType.SECRETARY) {
                response.setErrorCode(10034);
                return response;
            }

            ArrayList<Job> jobs = iJobService.loadJobToMatch(0, 100);
            response.setData(jobs);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10035);
                return response;
            }
        }

        return response;
    }

    /**
     * 分配一个任务给一个用户
     * Assign jobId to userId
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/addNewJobMatch")
    public Response addNewJobMatch(@RequestBody AdminRequest request,
                                   HttpServletRequest httpServletRequest) {
        /**
         * check whether the admin is rpg secretary
         * create a new jobMatch
         */
        Response response = new Response();
        try {
            if (request.getJobId() == null) {
                response.setErrorCode(10036);
                return response;
            }
            if (request.getUserId() == null) {
                response.setErrorCode(10036);
                return response;
            }
            String token = httpServletRequest.getHeader("token");
            Admin admin = iAdminService.loadAdminByToken(token);
            if (admin == null) {
                response.setErrorCode(10004);
                return response;
            }
            if (admin.getRoleType() != RoleType.SECRETARY) {
                response.setErrorCode(10034);
                return response;
            }
            JobMatch jobMatch = new JobMatch();
            jobMatch.setJobId(request.getJobId());
            jobMatch.setMatchUserId(request.getUserId());
            jobMatch.setMatchTime(new Date());
            iAdminService.matchJob(jobMatch);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10011);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取所有申请了jobId任务，且等待处理的用户
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/loadUsersAppliedJobAndWaiting")
    public Response LoadUsersAppliedJobAndWaiting(@RequestBody AdminRequest request,
                                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Admin admin = iAdminService.loadAdminByToken(token);
            if (admin == null) {
                response.setErrorCode(10004);
                return response;
            }
            if(admin.getRoleType()!=RoleType.SECRETARY){
                response.setErrorCode(10040);
                return response;
            }
            ArrayList<UserInfo> userInfos = iAdminService.loadJobAppliedUser(request.getJobId());
            response.setData(userInfos);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10029);
                return response;
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
