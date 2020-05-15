package com.gogoyang.rpgapi.framework.constant;

public enum JobStatus {
    /**
     * When a common has been published and waiting for match
     * 当任务已经发布，并等待匹配时
     * PENDING时甲方可以修改任务
     * 乙方可进行任务申请
     */
    PENDING,

    /**
     * When common has been applied or matched, but not yet dealt
     * 当任务已被人申请，或者被秘书分配，但未成交前
     * 乙方可进行任务申请
     */
    MATCHING,

    /**
     * When common has dealt and is progressing
     * 任务已成交，且正在进行中
     */
    PROGRESS,

    /**
     * When party b applied the common has been finished
     * 乙方申报任务已经完成，但还未被甲方验收
     */
    COMPLETE,

    /**
     * When common has been stopped
     * 任务已经被双方同意终止了
     */
    STOPPED,

    /**
     * When common has been accepted
     * 任务已验收成功
     */
    ACCEPTANCE,

    /**
     * The common is overdue
     * 任务已经过期
     */
    OVERDUE,

    /**
     * The common has been suspended by system for some reason
     * 任务被禁止
     */
    SUSPENDED

}
