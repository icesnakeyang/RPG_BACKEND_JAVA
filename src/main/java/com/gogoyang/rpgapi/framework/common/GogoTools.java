package com.gogoyang.rpgapi.framework.common;

import java.util.UUID;

public class GogoTools {
    public static String UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
