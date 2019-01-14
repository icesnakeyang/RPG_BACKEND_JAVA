package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import java.util.Map;

public interface ISecretaryMatchBusinessService {

    Map listAppliedJob(Map in) throws Exception;

    Map listApplyByJobId(Map in) throws Exception;

    void agreeApply(Map in) throws Exception;

    void rejectApply(Map in) throws Exception;

    Map getApplyJobTiny(Map in) throws Exception;

    Map getApplyJobDetail(Map in) throws Exception;

    Map listApplyHistory(Map in) throws Exception;

    Map getApplyDetail(Map in) throws Exception;
}
