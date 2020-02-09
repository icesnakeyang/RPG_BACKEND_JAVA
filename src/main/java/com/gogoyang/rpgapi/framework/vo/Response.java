package com.gogoyang.rpgapi.framework.vo;

import lombok.Data;

@Data
public class Response {
    private Integer errorCode=0;
    private String errorMsg="";
    private Object data;
}
