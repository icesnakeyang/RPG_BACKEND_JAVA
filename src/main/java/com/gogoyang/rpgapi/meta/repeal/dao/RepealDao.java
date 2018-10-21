package com.gogoyang.rpgapi.meta.repeal.dao;

import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepealDao extends JpaRepository<Repeal, Integer> {
    Page<Repeal> findAllByJobId(Integer jobId, Pageable pageable);

}
