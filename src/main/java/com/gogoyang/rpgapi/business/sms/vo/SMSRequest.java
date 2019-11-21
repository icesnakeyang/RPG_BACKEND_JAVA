package com.gogoyang.rpgapi.business.sms.vo;

import lombok.Data;

@Data
public class SMSRequest {
    private String phone;
    private String code;
    private String newPass;
}
