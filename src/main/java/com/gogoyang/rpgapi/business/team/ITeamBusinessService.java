package com.gogoyang.rpgapi.business.team;

import java.util.Map;

public interface ITeamBusinessService {
    Map createTeam(Map in) throws Exception;

    void addUserToTeam(Map in) throws Exception;

    Map listMyTeam(Map in) throws Exception;

    Map getTeamDetail(Map in) throws Exception;

    void addTeamMember(Map in) throws Exception;

    void getoffTeamMember(Map in) throws Exception;

    Map listTeamMember(Map in) throws Exception;
}
