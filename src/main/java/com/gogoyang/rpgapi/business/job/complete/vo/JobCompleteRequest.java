package com.gogoyang.rpgapi.business.job.complete.vo;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCompleteRequest {
    private Integer jobId;
    private String content;
    private LogStatus result;
    private String processRemark;

    /////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LogStatus getResult() {
        return result;
    }

    public void setResult(LogStatus result) {
        this.result = result;
    }

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }
}
