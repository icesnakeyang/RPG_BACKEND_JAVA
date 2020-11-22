package com.gogoyang.rpgapi.controller.admin.realname;

import lombok.Data;

@Data
public class SecretaryRealnameRequest {
    private Integer pageIndex;
    private Integer pageSize;
    private String userId;
    private String remark;
}
