package com.gogoyang.rpgapi.meta.userAction.dao;

import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionLogDao extends JpaRepository<UserActionLog, Integer> {

}
