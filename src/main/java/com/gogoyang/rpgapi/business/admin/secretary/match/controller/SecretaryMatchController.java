package com.gogoyang.rpgapi.business.admin.secretary.match.controller;

import com.gogoyang.rpgapi.business.admin.secretary.match.service.ISecretaryMatchBusinessService;
import com.gogoyang.rpgapi.business.admin.vo.AdminRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/secretary")
public class SecretaryMatchController {
    private final ISecretaryMatchBusinessService iSecretaryMatchBusinessService;

    @Autowired
    public SecretaryMatchController(ISecretaryMatchBusinessService iSecretaryMatchBusinessService) {
        this.iSecretaryMatchBusinessService = iSecretaryMatchBusinessService;
    }

    /**
     * 查询所有用户提交了申请的任务
     * rpg秘书查看用户提交的任务申请，选择用户分配。
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/loadJobToMatch")
    public Response loadJobToMatch(HttpServletRequest httpServletRequest) {
        /**
         * 检查admin是否存在
         * 检查admin是否有Secretary权限
         * 读取所有用户申请了任务
         */
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("pageIndex", 0);
            in.put("pageSize", 100);

            Map out=iSecretaryMatchBusinessService.loadJobToMatch(in);

            response.setData(out);
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
    @PostMapping("/matchJobToUser")
    public Response matchJobToUser(@RequestBody AdminRequest request,
                                   HttpServletRequest httpServletRequest) {
        /**
         * check whether the admin is rpg secretary
         * create a new jobMatch
         */
        Response response = new Response();
        try {
            Integer userId=request.getUserId();
            Integer jobId=request.getJobId();
            String token = httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("userId", userId);
            in.put("jobId", jobId);
            in.put("token", token);
            iSecretaryMatchBusinessService.matchJobToUser(in);
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


            /**
             * 根据jobId，查找所有申请过该任务，且未被处理的用户。
             *
             * @param jobId
             * @return
             * @throws Exception
             */
            @Override
            public ArrayList<UserInfo> loadJobAppliedUser(Integer jobId) throws Exception {
                /**
                 * 首先，根据JobId查找jobApplyLog，result==null
                 * 然后，查询JobMatch里是否已经分配了
                 * 根据jobApplyLog.applyUserId查找userInfo
                 * 返回所有的UserInfo
                 *
                 */
                ArrayList<JobApply> jobApplies = iJobApplyService.loadJobApplyByJobId(jobId);
                ArrayList<UserInfo> users = new ArrayList<UserInfo>();
                for (int i = 0; i < jobApplies.size(); i++) {
                    //检查是否已经分配
                    JobMatch jobMatch=iJobMatchService.loadJobMatchByUserIdAndJobId(
                            jobApplies.get(i).getApplyUserId(),
                            jobApplies.get(i).getJobId());
                    if(jobMatch==null) {
                        UserInfo userInfo = iUserInfoService.loadUserByUserId(jobApplies.get(i).getApplyUserId());
                        if (userInfo != null) {
                            users.add(userInfo);
                        }
                    }
                }

                return users;
            }


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
}
