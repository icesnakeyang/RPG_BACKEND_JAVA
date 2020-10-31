package com.gogoyang.rpgapi.framework.constant;

public enum GogoActType {
    REGISTER,
    LOGIN,
    CHECK_EMAIL,

    /**
     * 查看任务广场任务
     */
    LIST_PUBLIC_JOB,

    /**
     * 查看任务详情
     */
    LOAD_JOB_DETAIL,

    /**
     * 查看我的个人任务列表
     */
    LIST_MY_TASK,
    CREATE_TASK,
    DELETE_TASK,
    UPDATE_TASK,
    PUBLISH_TASK,
    CREATE_SUB_TASK,

    /**
     * 申请工作任务
     */
    APPLY_JOB,
    /**
     * 查看任务日志
     */
    LIST_JOB_LOG,
    CREATE_JOB_LOG,
    CREATE_JOB_COMPLETE,
    REJECT_JOB_COMPLETE,
    ACCEPT_JOB,
    LIST_ACCOUNT,
    UPDATE_JOB,
    LIST_SUB_TASK,
    DELETE_JOB_TASK_LOG,
    UPDATE_JOB_LOG,
    CREATE_TEAM,
    LIST_MY_TEAM
}
