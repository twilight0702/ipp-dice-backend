package com.ippclub.ippdicebackend;

import com.ippclub.ippdicebackend.service.RollService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RollTest {
    @Autowired
    private RollService rollService;

    @Test
    public void testRoll() {
        for(int i=0; i<10; i++) {
            System.out.println(rollService.roll());
        }
    }
}
