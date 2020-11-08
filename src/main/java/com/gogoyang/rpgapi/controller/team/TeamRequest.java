package com.gogoyang.rpgapi.controller.team;

import lombok.Data;

@Data
public class TeamRequest {
    private String description;
    private String teamName;
    private String teamId;
    private String userId;
}
