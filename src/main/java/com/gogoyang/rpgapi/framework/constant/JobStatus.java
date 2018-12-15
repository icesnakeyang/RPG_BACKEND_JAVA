package com.gogoyang.rpgapi.framework.constant;

public enum JobStatus {
    /**
     * When published and waiting for match
     * 任务发布后，等待匹配中
     */
    MATCHING,

    /**
     * When job has dealt and is progressing
     * 任务已成交，且正在进行中
     */
    PROGRESS,

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
     * When job is spotlighting in public
     * 当任务正在被申诉的时候
     */
    SPOTLIGHTING,

    /**
     * When the job spotlighting has been canceled by all party
     * 当任务申诉经双方同意，撤诉成功时
     */
    SPOTCANCELLED,

    /**
     * When a job has been published and waiting for match
     * 当任务已经发布，并等待匹配时
     */
    PENDING,

    /**
     * When user applied a new job and waiting for match
     * 当用户申请了一个任务，并等待处理时
     */
    APPLYING

}
