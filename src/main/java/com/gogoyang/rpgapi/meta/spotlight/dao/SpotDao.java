package com.gogoyang.rpgapi.meta.spotlight.dao;

import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpotDao extends JpaRepository<Spot, Integer> {
    /**
     * Read all list of spotlight of one job
     * @param jobId
     * @param pageable
     * @return
     */
    Page<Spot> findAllByJobId(Integer jobId, Pageable pageable);

    /**
     * Read detail of the spotlight
     * @param spotId
     * @return
     */
    Spot findBySpotId(Integer spotId);
}
