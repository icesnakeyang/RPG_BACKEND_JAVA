package com.gogoyang.rpgapi.business.job.repeal.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;
import com.gogoyang.rpgapi.meta.repeal.service.IRepealService;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import com.gogoyang.rpgapi.meta.spotlight.service.ISpotService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Service
public class RepealBusinessService implements IRepealBusinessService {
    private final IUserInfoService iUserInfoService;
    private final IJobService iJobService;
    private final ISpotService iSpotService;
    private final IRepealService iRepealService;

    @Autowired
    public RepealBusinessService(IUserInfoService iUserInfoService, IJobService iJobService, ISpotService iSpotService, IRepealService iRepealService) {
        this.iUserInfoService = iUserInfoService;
        this.iJobService = iJobService;
        this.iSpotService = iSpotService;
        this.iRepealService = iRepealService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Repeal createRepeal(Map in) throws Exception {
        /**
         * 创建一个撤诉申请
         * 根据token获取当前用户
         * 根据jobId获取当前任务
         * 只有甲方或者乙方能创建撤诉申请
         * 检查当前任务是否处于申诉状态
         * 检查当前是否有未处理的申请
         * 创建撤诉申请，jobstatus=SPOTLIGHTING
         */
        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        Integer jobId = (Integer) in.get("jobId");
        Job job = iJobService.loadJobByJobIdTiny(jobId);
        if (job == null) {
            throw new Exception("10084");
        }

        if (userInfo.getUserId() != job.getPartyAId()) {
            if (userInfo.getUserId() != job.getPartyBId()) {
                throw new Exception("10085");
            }
        }

        Spot spot = iSpotService.getSpotlightByJobId(jobId);
        if (spot == null) {
            throw new Exception("10086");
        }

        Repeal repeal = new Repeal();
        repeal.setContent(in.get("content").toString());
        repeal.setCreatedTime(new Date());
        repeal.setCreatedUserId(userInfo.getUserId());
        repeal.setJobId(jobId);
        repeal.setSpotId(spot.getSpotId());
        repeal = iRepealService.insertRepeal(repeal);
        return repeal;
    }

    @Override
    public Page<Repeal> listRepeal(Map in) throws Exception {
        /**
         * 读取jobId的所有Repeal
         * 把userId的name补充完整
         */
        Integer jobId = (Integer) in.get("jobId");
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        Page<Repeal> repeals = iRepealService.listRepealByJobId(jobId, pageIndex, pageSize);
        for(int i=0;i<repeals.getContent().size();i++){
            repeals.getContent().get(i).setCreatedUserName(iUserInfoService.getUserName(
                    repeals.getContent().get(i).getCreatedUserId()));
            if(repeals.getContent().get(i).getProcessUserId()!=null){
                repeals.getContent().get(i).setProcessUserName(iUserInfoService.getUserName(
                        repeals.getContent().get(i).getProcessUserId()));
            }
        }
        return repeals;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setRepealReadTime(Map in) throws Exception {
        /**
         * 读取jobId，userId的未读repeal
         * 设置readTime为当前服务器时间
         * 保存
         */
        Integer jobId=(Integer)in.get("jobId");
        String token=in.get("token").toString();
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        Repeal repeal=iRepealService.getMyUnReadRepeal(jobId, userInfo.getUserId());
        repeal.setReadTime(new Date());
        iRepealService.updateRepeal(repeal);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectRepeal(Map in) throws Exception {
        /**
         * 把repeal.processResult设置为reject
         * 设置处理用户和处理时间
         */
        Integer jobId=(Integer)in.get("jobId");
        String token=in.get("token").toString();
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        Repeal repeal=iRepealService.getUnProcessRepeal(jobId);

        if(repeal.getCreatedUserId()==userInfo.getUserId()){
            throw new Exception("10087");
        }

        if(in.get("processRemark")!=null) {
            repeal.setProcessRemark(in.get("processRemark").toString());
        }
        repeal.setProcessResult(LogStatus.REJECT);
        repeal.setProcessTime(new Date());
        repeal.setProcessUserId(userInfo.getUserId());
        iRepealService.updateRepeal(repeal);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptRepeal(Map in) throws Exception {
        /**
         * 同意撤诉
         * 1、处理撤诉repeal.processResult=accept
         * 2、处理spotlight，jobstatus=SPOTCANCELLED
         */
        String token=in.get("token").toString();
        String processRemark=in.get("processRemark").toString();
        Integer jobId=(Integer)in.get("jobId");
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);

        RepealBox box=loadRepealBox(jobId);

        if(userInfo.getUserId()!=box.getPartyA().getUserId()){
            if(userInfo.getUserId()!=box.getPartyB().getUserId()){
                throw new Exception("10085");
            }
        }

        if(box.getSpotlight()==null){
            throw new Exception("10086");
        }

        Repeal repeal=iRepealService.getUnProcessRepeal(jobId);

        if(repeal.getCreatedUserId()==userInfo.getUserId()){
            throw new Exception("10087");
        }

        repeal.setProcessUserId(userInfo.getUserId());
        repeal.setProcessTime(new Date());
        repeal.setProcessResult(LogStatus.ACCEPT);
        repeal.setProcessRemark(processRemark);
        iRepealService.updateRepeal(repeal);

        box.getSpotlight().setJobStatus(JobStatus.SPOTCANCELLED);
        iSpotService.updateSpotlight(box.getSpotlight());
    }

    @Override
    public Integer countUnreadRepeal(Map in) throws Exception {
        /**
         * 读取当前userId，jobId的未阅读的repeal
         * 统计返回数量
         */
        Integer jobId=(Integer)in.get("jobId");
        String token=in.get("token").toString();
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        Repeal repeal=iRepealService.getMyUnReadRepeal(jobId, userInfo.getUserId());
        if(repeal==null) {
            return 0;
        }else {
            return 1;
        }
    }

    private RepealBox loadRepealBox(Integer jobId) throws Exception{
        Job job=iJobService.loadJobByJobIdTiny(jobId);
        UserInfo partyA=iUserInfoService.loadUserByUserId(job.getPartyAId());
        UserInfo partyB=iUserInfoService.loadUserByUserId(job.getPartyBId());
        Spot spot=iSpotService.getSpotlightByJobId(jobId);

        RepealBox repealBox=new RepealBox();
        repealBox.setJob(job);
        repealBox.setPartyA(partyA);
        repealBox.setPartyB(partyB);
        repealBox.setSpotlight(spot);

        return repealBox;
    }
}
