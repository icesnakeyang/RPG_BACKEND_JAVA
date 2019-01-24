package com.gogoyang.rpgapi.meta.log.dao;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface JobLogDao extends JpaRepository<JobLog, Integer> {
    /**
     * if user is party a，createdUser!=user，partyAid==user
     */
    ArrayList<JobLog> findAllByReadTimeIsNullAndCreatedUserIdIsNotAndPartyAId(Integer createdUserId, Integer partyAId);

    /**
     * read all unread log by party a of job id
     */
    ArrayList<JobLog> findAllByReadTimeIsNullAndCreatedUserIdIsNotAndPartyAIdAndJobId(Integer createdUserId, Integer partyAId, Integer jobId);

    /**
     * if user is party b，createdUser!=user, partyBid==user
     */
    ArrayList<JobLog> findAllByReadTimeIsNullAndCreatedUserIdIsNotAndPartyBId(Integer createdUserId, Integer partyBId);

    /**
     * read all unread log by party b of job id
     */
    ArrayList<JobLog> findAllByReadTimeIsNullAndCreatedUserIdIsNotAndPartyBIdAndJobId(Integer createdUserId, Integer partyBId, Integer jobId);

    Page<JobLog> findAllByJobId(Integer jobId, Pageable pageable);
}
