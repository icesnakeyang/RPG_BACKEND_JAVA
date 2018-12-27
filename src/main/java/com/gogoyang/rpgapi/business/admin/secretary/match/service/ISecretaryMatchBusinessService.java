package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import java.util.Map;

public interface ISecretaryMatchBusinessService {
    Map listJobToMatch(Map in) throws Exception;

    void matchJobToUser(Map in) throws Exception;

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
    Map listApplyHistory(Map in) throws Exception;

    Map listJobMatching(Map in) throws Exception;

    Map listApplyByJobId(Map in) throws Exception;
}
