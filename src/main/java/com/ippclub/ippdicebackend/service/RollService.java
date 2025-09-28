package com.ippclub.ippdicebackend.service;

import com.ippclub.ippdicebackend.bo.DiceOutcome;
import com.ippclub.ippdicebackend.bo.RollResult;
import org.springframework.stereotype.Service;

import java.util.*;
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
        DiceOutcome outcome = match(dices);
        Integer score = outcome.calScore(dices);

        return RollResult.of(dices, outcome, score);
    }



    /**
     * 根据骰子点数列表返回判定结果（优先级最高的先匹配）
     */
    public static DiceOutcome match(List<Integer> dice) {
        return Arrays.stream(DiceOutcome.values())
                .filter(outcome -> matches(outcome, dice))
                .max(Comparator.comparingInt(DiceOutcome::getRank))
                .orElse(DiceOutcome.PU_TONG);
    }

    private static boolean matches(DiceOutcome outcome, List<Integer> dice) {
        Map<Integer, Long> countMap = getCountMap(dice);
        return switch (outcome) {
            case LIU_PAO_HONG -> dice.stream().allMatch(d -> d == 4);
            case ZHUANG_YUAN_CJH -> countMap.getOrDefault(4, 0L) == 4 && countMap.getOrDefault(1, 0L) == 2;
            case WU_HUANG -> countMap.getOrDefault(4, 0L) == 5;
            case WU_ZI -> countMap.values().stream().anyMatch(c -> c == 5) && countMap.getOrDefault(4, 0L) != 5;
            case ZHUANG_YUAN -> countMap.getOrDefault(4, 0L) == 4;
            case DUI_TANG -> {
                List<Integer> sorted = dice.stream().sorted().toList();
                yield sorted.equals(Arrays.asList(1, 2, 3, 4, 5, 6));
            }
            case SAN_HONG -> countMap.getOrDefault(4, 0L) == 3;
            case SI_JIN -> countMap.entrySet().stream().anyMatch(e -> e.getValue() == 4 && e.getKey() != 4);
            case ER_JU -> countMap.getOrDefault(4, 0L) == 2;
            case YI_XIU -> countMap.getOrDefault(4, 0L) == 1;
            case PU_TONG -> true;
        };
    }

    private static Map<Integer, Long> getCountMap(List<Integer> dice) {
        return dice.stream()
                .collect(Collectors.groupingBy(d -> d, Collectors.counting()));
    }
}