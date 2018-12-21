package com.gogoyang.rpgapi.framework.constant;

public enum AccountType {
    TOP_UP,        //充值
    WITHDRAW,      //取现
    PUBLISH,       //发布任务
    ACCEPT,        //验收任务
    REFUND_OUT,    //退还任务金额给对方
    REFUND_IN      //收到对方的退换任务金额
}
