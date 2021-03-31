package com.gogoyang.rpgapi.controller.admin.withdraw;

import com.gogoyang.rpgapi.framework.vo.Request;
import lombok.Data;

@Data
public class UserWithdrawRequest extends Request {
    private String withdrawLedgerId;

}
