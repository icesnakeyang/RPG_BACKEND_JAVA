package com.gogoyang.rpgapi.controller.account;

import com.gogoyang.rpgapi.framework.vo.Request;
import lombok.Data;

@Data
public class AccountRequest extends Request {
    private Integer userId;
    private Double amount;
    private String remark;
}
