package com.gogoyang.rpgapi.business.team;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.meta.team.entity.Team;
import com.gogoyang.rpgapi.meta.team.service.ITeamService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TeamBusinessService implements ITeamBusinessService {
    private final ICommonBusinessService iCommonBusinessService;
    private final ITeamService iTeamService;

    public TeamBusinessService(ICommonBusinessService iCommonBusinessService,
                               ITeamService iTeamService) {
        this.iCommonBusinessService = iCommonBusinessService;
        this.iTeamService = iTeamService;
    }

    /**
     * 创建一个团队
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createTeam(Map in) throws Exception {
        String token = in.get("token").toString();
        String description = (String) in.get("description");
        String teamName = in.get("teamName").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = new Team();
        team.setCreateTime(new Date());
        team.setCreateUserId(userInfo.getUserId());
        team.setDescription(description);
        team.setManagerId(userInfo.getUserId());
        team.setStatus(GogoStatus.ACTIVE.toString());
        team.setTeamId(GogoTools.UUID().toLowerCase());
        team.setTeamName(teamName);
        iTeamService.createTeam(team);
    }

    @Override
    public void addUserToTeam(Map in) throws Exception {

    }

    @Override
    public Map listMyTeam(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", userInfo.getUserId());
        ArrayList<Team> teams = iTeamService.listTeam(qIn);

        Map out = new HashMap();
        out.put("teamList", teams);

        return out;
    }
}
