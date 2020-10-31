package com.gogoyang.rpgapi.meta.team.service;

import com.gogoyang.rpgapi.meta.team.entity.Team;

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
}
