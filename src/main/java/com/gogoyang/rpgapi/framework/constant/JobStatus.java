package com.gogoyang.rpgapi.framework.constant;

public enum JobStatus {
    /**
     * When a job has been published and waiting for match
     * 当任务已经发布，并等待匹配时
     */
    PENDING,

    /**
     * When job has been applied or matched, but not yet dealt
     * 当任务已被人申请，或者被秘书分配，但未成交前
     */
    MATCHING,

    /**
     * When job has dealt and is progressing
     * 任务已成交，且正在进行中
     */
    PROGRESS,

    /**
     * When party b applied the job has been finished
     * 乙方申报任务已经完成，但还未被甲方验收
     */
    COMPLETE,

    /**
     * When job has been stopped
     * 任务已经被双方同意终止了
     */
    STOPPED,

    /**
     * When job has been accepted
     * 任务已验收成功
     */
    ACCEPTANCE,

    /**
     * The job is overdue
     * 任务已经过期
     */
    OVERDUE,

    /**
     * The job has been suspended by system for some reason
     * 任务被禁止
     */
    SUSPENDED

}
