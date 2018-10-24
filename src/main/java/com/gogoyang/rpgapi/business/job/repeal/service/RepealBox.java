package com.gogoyang.rpgapi.business.job.repeal.service;

import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;

public class RepealBox {
    private UserInfo partyA;
    private UserInfo partyB;
    private Job job;
    private Spot spotlight;

    //////////////////////////////////////////////////////////////////////////////

    public UserInfo getPartyA() {
        return partyA;
    }

    public void setPartyA(UserInfo partyA) {
        this.partyA = partyA;
    }

    public UserInfo getPartyB() {
        return partyB;
    }

    public void setPartyB(UserInfo partyB) {
        this.partyB = partyB;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Spot getSpotlight() {
        return spotlight;
    }

    public void setSpotlight(Spot spotlight) {
        this.spotlight = spotlight;
    }
}
