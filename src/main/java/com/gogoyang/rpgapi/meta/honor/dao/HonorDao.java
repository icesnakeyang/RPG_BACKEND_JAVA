package com.gogoyang.rpgapi.meta.honor.dao;

import com.gogoyang.rpgapi.framework.constant.HonorType;
import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HonorDao extends JpaRepository<Honor, Integer> {
    Page<Honor> findAllByUserId(Integer userId, Pageable pageable);

    @Query(nativeQuery = true, value = "select sum(point) from honor o where user_id=:userId and type=:honorType")
    Long loadSumPoint(@Param("userId")Integer userId, @Param("honorType")Integer honorType);
}
