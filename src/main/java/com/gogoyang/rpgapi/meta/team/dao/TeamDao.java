package com.gogoyang.rpgapi.meta.team.dao;

import com.gogoyang.rpgapi.meta.team.entity.Team;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeamDao {
    /**
     * 创建一个团队
     * @param team
     */
    void createTeam(Team team);
}
