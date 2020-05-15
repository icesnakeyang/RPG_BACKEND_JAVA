package com.gogoyang.rpgapi.meta.account.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户提现日志
 */
@Data
public class WithdrawLedger {
    /**
     * 自动递增id
     */
    private Integer ids;

    /**
     * 唯一识别码
     */
    private String withdrawLedgerId;

    /**
     * 申请取现用户
     */
    private String userId;

    /**
     * 取现申请时间
     */
    private Date createTime;

    /**
     * 取现金额
     */
    private Double amount;

    /**
     * 用户提现时的说明
     */
    private String remark;

    /**
     * 当前状态
     */
    private String status;

    /**
     * 申请被管理员查看的时间
     */
    private Date readTime;

    /**
     * 申请被管理员处理的时间
     */
    private Date processTime;

    /**
     * 处理说明
     */
    private String processRemark;

    /**
     * 处理结果被提现申请用户查看的时间
     */
    private Date processReadTime;
}
