package com.gogoyang.rpgapi.controller.team;

import com.gogoyang.rpgapi.business.team.ITeamBusinessService;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("rpgapi/team")
public class TeamController {
    private final ITeamBusinessService iTeamBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    public TeamController(ITeamBusinessService iTeamBusinessService,
                          ICommonBusinessService iCommonBusinessService) {
        this.iTeamBusinessService = iTeamBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 创建一个团队
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createTeam")
    public Response createTeam(@RequestBody TeamRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("description", request.getDescription());
            in.put("teamName", request.getTeamName());

            logMap.put("GogoActType", GogoActType.CREATE_TEAM);
            logMap.put("token", token);
            memoMap.put("teamName", request.getTeamName());

            iTeamBusinessService.createTeam(in);

            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                log.error("createTeam:" + ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            log.error("User action log error:" + ex3.getMessage());
        }
        return response;
    }

    /**
     * 查询我的团队列表
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyTeam")
    public Response listMyTeam(@RequestBody TeamRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("description", request.getDescription());
            in.put("teamName", request.getTeamName());

            logMap.put("GogoActType", GogoActType.LIST_MY_TEAM);
            logMap.put("token", token);

            Map out = iTeamBusinessService.listMyTeam(in);
            response.setData(out);

            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                log.error("listMyTeam:" + ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            log.error("User action log error:" + ex3.getMessage());
        }
        return response;
    }

    /**
     * 查询团队详情
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/getTeamDetail")
    public Response getTeamDetail(@RequestBody TeamRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("teamId", request.getTeamId());

            logMap.put("GogoActType", GogoActType.GET_TEAM_DEAIL);
            logMap.put("token", token);
            memoMap.put("teamId", request.getTeamId());

            Map out = iTeamBusinessService.getTeamDetail(in);
            response.setData(out);

            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                log.error("listMyTeam:" + ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            log.error("User action log error:" + ex3.getMessage());
        }
        return response;
    }

    /**
     * 添加一个团队成员
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/addTeamMember")
    public Response addTeamMember(@RequestBody TeamRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("teamId", request.getTeamId());
            in.put("userId", request.getUserId());

            logMap.put("GogoActType", GogoActType.ADD_TEAM_MEMBER);
            logMap.put("token", token);
            memoMap.put("teamId", request.getTeamId());
            memoMap.put("userId", request.getUserId());

            iTeamBusinessService.addTeamMember(in);

            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                log.error("listMyTeam:" + ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            log.error("User action log error:" + ex3.getMessage());
        }
        return response;
    }
}
