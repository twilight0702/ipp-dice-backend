package com.ippclub.ippdicebackend.service;

import com.ippclub.ippdicebackend.bo.DiceOutcome;
import com.ippclub.ippdicebackend.bo.RollResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 骰子相关服务
 */
@Service
public class RollService {
    /**
     * 随机骰子点数，得到结果
     */
    public RollResult roll() {
        List<Integer> dices = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dices.add(ThreadLocalRandom.current().nextInt(1, 7)); // [1,7) -> 1~6
        }
        return RollResult.of(dices, DiceOutcome.fromDice(dices));
    }
}