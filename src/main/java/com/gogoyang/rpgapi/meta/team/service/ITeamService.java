package com.gogoyang.rpgapi.meta.team.service;

import com.gogoyang.rpgapi.meta.team.entity.Team;

public interface ITeamService {
    /**
     * 创建一个团队
     * @param team
     */
    void createTeam(Team team);
}
