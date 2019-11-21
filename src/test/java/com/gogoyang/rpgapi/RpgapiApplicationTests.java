package com.gogoyang.rpgapi;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RpgapiApplicationTests {
    @Autowired
    private IRPGFunction irpgFunction;

    @Test
    public void contextLoads() {
        Map in = new HashMap();
        in.put("phone", "13438010117");
        try {
            irpgFunction.sendMSM(in);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
