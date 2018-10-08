package com.gogoyang.rpgapi.business.admin.secretary.match;

public class SecretaryMatchController {

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
}
