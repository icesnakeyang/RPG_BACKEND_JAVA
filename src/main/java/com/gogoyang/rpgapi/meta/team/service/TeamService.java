package com.gogoyang.rpgapi.meta.team.service;

import com.gogoyang.rpgapi.meta.team.dao.TeamDao;
import com.gogoyang.rpgapi.meta.team.dao.TeamUserDao;
import com.gogoyang.rpgapi.meta.team.entity.Team;
import com.gogoyang.rpgapi.meta.team.entity.TeamUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class TeamService implements ITeamService {
    private final TeamDao teamDao;
    private final TeamUserDao teamUserDao;

    public TeamService(TeamDao teamDao,
                       TeamUserDao teamUserDao) {
        this.teamDao = teamDao;
        this.teamUserDao = teamUserDao;
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

    @Override
    public Team getTeamDetail(String teamId) {
        Team team = teamDao.getTeamDetail(teamId);
        return team;
    }

    @Override
    public void createTeamUser(TeamUser teamUser) {
        teamUserDao.createTeamUser(teamUser);
    }

    @Override
    public ArrayList<TeamUser> listTeamUser(Map qIn) {
        ArrayList<TeamUser> teamUsers = teamUserDao.listTeamUser(qIn);
        return teamUsers;
    }

    @Override
    public void deleteTeamUser(Integer ids) {
        teamUserDao.deleteTeamUser(ids);
    }
}
