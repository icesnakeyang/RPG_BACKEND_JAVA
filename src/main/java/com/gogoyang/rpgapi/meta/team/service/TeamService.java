package com.gogoyang.rpgapi.meta.team.service;

import com.gogoyang.rpgapi.meta.team.dao.TeamDao;
import com.gogoyang.rpgapi.meta.team.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService implements ITeamService{
    private final TeamDao teamDao;

    public TeamService(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    @Override
    public void createTeam(Team team) {
        teamDao.createTeam(team);
    }
}
