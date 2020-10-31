package com.gogoyang.rpgapi.meta.team.dao;

import com.gogoyang.rpgapi.meta.team.entity.Team;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TeamDao {
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
