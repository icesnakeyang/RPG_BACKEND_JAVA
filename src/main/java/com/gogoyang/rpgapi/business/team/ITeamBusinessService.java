package com.gogoyang.rpgapi.business.team;

import java.util.Map;

public interface ITeamBusinessService {
    void createTeam(Map in) throws Exception;

    void addUserToTeam(Map in) throws Exception;

    Map listMyTeam(Map in) throws Exception;
}
