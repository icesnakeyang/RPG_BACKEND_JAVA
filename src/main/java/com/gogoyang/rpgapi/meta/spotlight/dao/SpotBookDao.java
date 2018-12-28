package com.gogoyang.rpgapi.meta.spotlight.dao;

import com.gogoyang.rpgapi.meta.spotlight.entity.SpotBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotBookDao extends JpaRepository<SpotBook, Integer> {
    Page<SpotBook> findAllBySpotId(Integer spotId, Pageable pageable);
}
