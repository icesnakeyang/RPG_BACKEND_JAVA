package com.gogoyang.rpgapi.meta.spotlight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SpotContent {
    @Id
    @Column(name = "spot_id")
    private Integer spotId;

    @Column(name = "content")
    private String content;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getSpotId() {
        return spotId;
    }

    public void setSpotId(Integer spotId) {
        this.spotId = spotId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
