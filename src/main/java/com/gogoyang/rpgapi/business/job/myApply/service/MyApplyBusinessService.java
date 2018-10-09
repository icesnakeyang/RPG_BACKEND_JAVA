package com.gogoyang.rpgapi.business.job.myApply.service;

import org.springframework.stereotype.Service;

@Service
public class MyApplyBusinessService implements IMyApplyBusinessService{

    /**
     * 读取所有我申请的，还未处理的任务。
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList loadMyApplyJob(Integer userId) throws Exception {
        ArrayList<JobApply> myApplyList = iJobApplyService.loadMyApplies(userId);
        ArrayList jobList = new ArrayList();
        Map mapOut = new HashMap();
        for (int i = 0; i < myApplyList.size(); i++) {
            Job job = jobDao.findByJobId(myApplyList.get(i).getJobId());
            if (job != null) {
                job.setPartyAName(iUserInfoService.getUserName(job.getPartyAId()));
                Integer applyNum = iJobApplyService.countApplyUsers(job.getJobId());
                Integer matchNum = iJobMatchService.countMatchingUsers(job.getJobId());
                Map theMap = new HashMap();
                theMap.put("job", job);
                theMap.put("apply", myApplyList.get(i));
                theMap.put("applyNum", applyNum);
                theMap.put("matchNum", matchNum);

                jobList.add(theMap);
            }
        }

        return jobList;
    }
}
