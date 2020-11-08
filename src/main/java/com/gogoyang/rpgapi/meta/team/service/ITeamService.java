package com.gogoyang.rpgapi.meta.team.service;

import com.gogoyang.rpgapi.meta.team.entity.Team;
import com.gogoyang.rpgapi.meta.team.entity.TeamUser;

import java.util.ArrayList;
import java.util.Map;

public interface ITeamService {
    /**
     * 创建一个团队
     * @param team
     */
    void createTeam(Team team);

    /**
     * 查询团队列表
     * @param qIn
     * userId
     * @return
     */
    ArrayList<Team> listTeam(Map qIn);

    Team getTeamDetail(String teamId);

    /**
     * 创建一个团队成员
     * @param teamUser
     */
    void createTeamUser(TeamUser teamUser);

    /**
     * 查询团队成员列表
     * @param qIn
     * userId
     * teamId
     * status
     * @return
     */
    ArrayList<TeamUser> listTeamUser(Map qIn);
}
