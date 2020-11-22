package com.gogoyang.rpgapi.meta.team.dao;

import com.gogoyang.rpgapi.meta.team.entity.TeamUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamUserDao {
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

    void deleteTeamUser(Integer ids);
}
