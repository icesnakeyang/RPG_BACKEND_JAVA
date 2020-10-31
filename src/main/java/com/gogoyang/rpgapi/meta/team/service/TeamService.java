package com.gogoyang.rpgapi.meta.team.service;

import com.gogoyang.rpgapi.meta.team.dao.TeamDao;
import com.gogoyang.rpgapi.meta.team.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class TeamService implements ITeamService {
    private final TeamDao teamDao;

    public TeamService(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    /**
     * 创建一个团队
     *
     * @param team
     */
    @Override
    public void createTeam(Team team) {
        teamDao.createTeam(team);
    }

    /**
     * 查询团队列表
     *
     * @param qIn userId
     * @return
     */
    @Override
    public ArrayList<Team> listTeam(Map qIn) {
        ArrayList<Team> teams = teamDao.listTeam(qIn);
        return teams;
    }
}
