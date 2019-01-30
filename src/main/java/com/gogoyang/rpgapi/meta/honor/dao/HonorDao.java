package com.gogoyang.rpgapi.meta.honor.dao;

import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HonorDao extends JpaRepository<Honor, Integer> {
    Page<Honor> findAllByUserId(Integer userId, Pageable pageable);
}
