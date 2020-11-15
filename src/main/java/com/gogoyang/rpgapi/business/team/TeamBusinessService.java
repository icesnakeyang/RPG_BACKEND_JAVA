package com.gogoyang.rpgapi.business.team;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.meta.team.entity.Team;
import com.gogoyang.rpgapi.meta.team.entity.TeamUser;
import com.gogoyang.rpgapi.meta.team.service.ITeamService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.SSLEngineResult;
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

    @Override
    public Map getTeamDetail(Map in) throws Exception {
        String token = in.get("token").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo = iCommonBusinessService.getUserByToken(token);

        Team team = iCommonBusinessService.getTeamDetail(teamId);

        Map out = new HashMap();
        out.put("team", team);

        /**
         * 读取团队成员列表
         */
        Map qIn=new HashMap();
        qIn.put("teamId", team.getTeamId());
        ArrayList<TeamUser> teamUsers=iTeamService.listTeamUser(qIn);

        out.put("teamUserList", teamUsers);

        return out;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addTeamMember(Map in) throws Exception {
        String token = in.get("token").toString();
        String userId = in.get("userId").toString();
        String teamId = in.get("teamId").toString();

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);

        UserInfo addUser=iCommonBusinessService.getUserByUserId(userId);

        Team team=iCommonBusinessService.getTeamDetail(teamId);

        if(!team.getManagerId().equals(userInfo.getUserId())){
            //不是团队管理员，不能添加成员
            throw new Exception("30025");
        }
        if(userInfo.getUserId().equals(userId)){
            //不能添加自己
            throw new Exception("30026");
        }

        /**
         * 检查是否已经添加过了
         */
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        qIn.put("teamId", teamId);
        qIn.put("status", GogoStatus.ACTIVE);
        ArrayList<TeamUser> teamUsers=iTeamService.listTeamUser(qIn);
        if(teamUsers.size()>0){
            //已经添加了
            throw new Exception("30027");
        }

        TeamUser teamUser=new TeamUser();
        teamUser.setCreateTime(new Date());
        teamUser.setUserId(userId);
        teamUser.setTeamId(teamId);
        teamUser.setStatus(GogoStatus.ACTIVE.toString());
        teamUser.setMemberType(GogoStatus.NORMAL.toString());
        iTeamService.createTeamUser(teamUser);
    }
}
