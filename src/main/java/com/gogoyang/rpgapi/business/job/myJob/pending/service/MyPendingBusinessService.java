package com.gogoyang.rpgapi.business.job.myJob.pending.service;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.resource.entity.ResourceFile;
import com.gogoyang.rpgapi.meta.resource.service.IResourceFileService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyPendingBusinessService implements IMyPendingBusinessService {
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final IJobApplyService iJobApplyService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IResourceFileService iResourceFileService;
    private final IRPGFunction irpgFunction;

    @Autowired
    public MyPendingBusinessService(IJobService iJobService,
                                    IUserService iUserService,
                                    IJobApplyService iJobApplyService,
                                    ICommonBusinessService iCommonBusinessService,
                                    IResourceFileService iResourceFileService,
                                    IRPGFunction irpgFunction) {
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iJobApplyService = iJobApplyService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iResourceFileService = iResourceFileService;
        this.irpgFunction = irpgFunction;
    }

    @Override
    public Map listMyPendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo user = iUserService.getUserByToken(token);

        String partyAId = user.getUserId();
        ArrayList<Job> jobs = iJobService.listMyPendingJob(partyAId, pageIndex, pageSize);
        Map out = new HashMap();
        out.put("jobs", jobs);
        return out;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(Map in) throws Exception {
        /**
         * 首先检查用户
         * 读取job
         * 检查job状态是否为pending
         * 检查用户是否为partyA
         * 修改job和jobDetail
         */
        String token = in.get("token").toString();
        String jobId = (String) in.get("jobId");
        String title = in.get("title").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        Double price = (Double) in.get("price");
        Map detailMap =(Map) in.get("detailMap");

        UserInfo user = iUserService.getUserByToken(token);

        if (user == null) {
            throw new Exception("10004");
        }

        Job job = iJobService.getJobTinyByJobId(jobId);

        if (job == null) {
            throw new Exception("10100");
        }

        if (!job.getStatus().equals(JobStatus.PENDING.toString())) {
            if(job.getStatus().equals(JobStatus.MATCHING.toString())){
                //任务已经有人申请了，不能修改了。
                throw new Exception("30017");
            }
            //任务不是等待申请状态，不能修改了
            throw new Exception("30018");
        }

        if (!user.getUserId().equals(job.getPartyAId())) {
            throw new Exception("10102");
        }

        //修改job
        job.setTitle(title);
        job.setCode(code);
        job.setDays(days);
        job.setPrice(price);
        job.setDetail(detailMap.get("detail").toString());
        iJobService.updateJob(job);

        /**
         * 如果有新的删除原来的资源文件，重新保存资源文件
         */

        //读取原来的资源文件
        Map qIn = new HashMap();
        qIn.put("jobId", job.getJobId());
        ArrayList<ResourceFile> resourceFiles = iResourceFileService.listResourceFile(qIn);

        for (int i = 0; i < resourceFiles.size(); i++) {
            //判断该文件是否还在detail，没有就删除oss服务器上的文件
            String fileName = resourceFiles.get(i).getFileName();
            if (fileName != null) {
                Integer fileIndex = job.getDetail().indexOf(fileName);
                if (fileIndex == -1) {
                    //检查task detail里是否引用了该图片
                    if(resourceFiles.get(i).getTaskId()==null) {
                        //图片已经没有了，删除
                        irpgFunction.deleteOSSFile(fileName);
                        //删除资源文件库
                        qIn=new HashMap();
                        qIn.put("fileId", resourceFiles.get(i).getFileId());
                        iResourceFileService.deleteResourceFile(qIn);
                    }else {
                        //task里还有该图片，把jobId设置为null，删除task里的图片时，就可以删除该资源了
                        qIn=new HashMap();
                        qIn.put("jobIdNull", true);
                        qIn.put("fileId", resourceFiles.get(i).getFileId());
                        iResourceFileService.updateResourceFile(qIn);
                    }
                }
            }
        }

        //如果有新的图片，就添加资源库
        ArrayList fileList = (ArrayList) detailMap.get("fileList");
        for (int i = 0; i < fileList.size(); i++) {
            String url = ((Map) fileList.get(i)).get("url").toString();
            String fileName = ((Map) fileList.get(i)).get("fileName").toString();
            ResourceFile resourceFile = new ResourceFile();
            resourceFile.setCreateTime(new Date());
            resourceFile.setFileId(GogoTools.UUID());
            resourceFile.setFileName(fileName);
            resourceFile.setJobId(job.getJobId());
            resourceFile.setUrl(url);
            iResourceFileService.createResourceFile(resourceFile);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId = in.get("jobId").toString();

        //read current user
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //read the job
        Job job = iJobService.getJobTinyByJobId(jobId);
        if (job == null) {
            //no such job
            throw new Exception("30001");
        }
        //check the job status must be PENDING or MATCHING
        if (!job.getStatus().equals(JobStatus.PENDING.toString()) &&
                !job.getStatus().equals(JobStatus.MATCHING)) {
            throw new Exception("30005");
        }

        /**
         * 检查是否有未处理的乙方申请
         */
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("status", LogStatus.PENDING.toString());
        ArrayList<JobApply> jobApplies = iJobApplyService.listJobApply(qIn);
        if (jobApplies.size() > 0) {
            //存在未处理的用户申请，不能删除任务
            throw new Exception("10104");
        }
        //check the party a of this job must be current user
        if (!job.getPartyAId().equals(user.getUserId())) {
            throw new Exception("10105");
        }
        //delete
        iJobService.deleteJob(jobId);

        /**
         * 删除原来的资源文件，重新保存资源文件
         */
        //读取原来的资源文件
        qIn=new HashMap();
        qIn.put("jobId", job.getJobId());
        ArrayList<ResourceFile> resourceFiles=iResourceFileService.listResourceFile(qIn);

        for(int i=0;i<resourceFiles.size();i++){
            //删除oss服务器上的文件
            String fileName=resourceFiles.get(i).getFileName();
            if(fileName!=null){
                irpgFunction.deleteOSSFile(fileName);
            }
            //删除资源文件库
            if(resourceFiles.get(i).getTaskId()==null) {
                //如果taskId不为空，则改资源也被task引用，不能删除
                iResourceFileService.deleteResourceFile(qIn);
            }
        }
    }

    @Override
    public Map getMyPendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  in.get("jobId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Job job = iJobService.getJobDetailByJobId(jobId);

        job.setTotalApply(iJobApplyService.countApplyUsers(job.getJobId()));

        Map out = new HashMap();
        out.put("job", job);
        return out;
    }
}
