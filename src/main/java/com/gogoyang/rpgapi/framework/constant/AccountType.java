package com.gogoyang.rpgapi.framework.constant;

public enum AccountType {
    TOP_UP,        //充值
    WITHDRAW,      //取现
    PUBLISH,       //发布任务
    APPLY_SUCCESS,        //申请任务成功，当用户申请了任务，管理员同意后，即刻把任务金额转给乙方用户
    REFUND_OUT,    //退还任务金额给对方
    REFUND_IN      //收到对方的退换任务金额
}
