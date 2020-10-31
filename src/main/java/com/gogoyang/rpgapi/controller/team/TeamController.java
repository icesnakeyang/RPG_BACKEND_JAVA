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

            logMap.put("", GogoActType.CREATE_TEAM);
            logMap.put("token", token);
            memoMap.put("teamName", request.getTeamName());

            iTeamBusinessService.createTeam(in);

            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10001);
                log.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            log.error(ex3.getMessage());
        }
        return response;
    }
}
