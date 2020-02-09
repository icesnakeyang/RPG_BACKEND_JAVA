package com.gogoyang.rpgapi.controller.account;

import com.gogoyang.rpgapi.controller.vo.Request;
import lombok.Data;

@Data
public class AccountRequest extends Request {
    private Integer userId;
}
