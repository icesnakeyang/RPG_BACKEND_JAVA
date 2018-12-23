package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import java.util.Map;

public interface ISecretaryMatchBusinessService {
    Map listJobToMatch(Map in) throws Exception;

    void matchJobToUser(Map in) throws Exception;

    Map loadUserApplyJob(Map in) throws Exception;

    /**
     * list all users who applied jobs
     */
    Map listUserAppliedJob(Map in) throws Exception;

    /**
     * list user history
     * @param in
     * @return
     * @throws Exception
     */
    Map listHistory(Map in) throws Exception;

    Map listNewApply(Map in) throws Exception;
}
