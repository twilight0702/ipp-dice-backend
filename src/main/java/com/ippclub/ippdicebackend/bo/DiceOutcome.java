package com.ippclub.ippdicebackend.bo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.*;
import java.util.stream.Collectors;

public enum DiceOutcome {
    LIU_PAO_HONG("六抔红", 100) {
        @Override
        public boolean matches(List<Integer> dice) {
            return dice.stream().allMatch(d -> d == 4);
        }

        @Override
        public int calScore(List<Integer> dice) {
            // 六抔红无需进一步比较
            return DiceOutcome.LIU_PAO_HONG.rank;
        }
    },
    ZHUANG_YUAN_CJH("状元插金花", 99) {
        @Override
        public boolean matches(List<Integer> dice) {
            Map<Integer, Long> countMap = getCountMap(dice);
            return countMap.getOrDefault(4, 0L) == 4 && countMap.getOrDefault(1, 0L) == 2;
        }
        
        @Override
        public int calScore(List<Integer> dice) {
            // 状元插金花无需进一步比较（除非有多个，但规则中未说明）
            return DiceOutcome.ZHUANG_YUAN_CJH.rank;
        }
    },
    WU_HUANG("五红", 90) {
        @Override
        public boolean matches(List<Integer> dice) {
            Map<Integer, Long> countMap = getCountMap(dice);
            return countMap.getOrDefault(4, 0L) == 5;
        }
        
        @Override
        public int calScore(List<Integer> dice) {
            // 比较非四点的点数大小
            int nonFour1 = dice.stream().filter(d -> d != 4).findFirst().orElse(0);
            return DiceOutcome.WU_HUANG.rank + nonFour1;
        }
    },
    WU_ZI("五子登科", 80) {
        @Override
        public boolean matches(List<Integer> dice) {
            Map<Integer, Long> countMap = getCountMap(dice);
            return countMap.values().stream().anyMatch(c -> c == 5) && countMap.getOrDefault(4, 0L) != 5;
        }
        
        @Override
        public int calScore(List<Integer> dice) {
            // 比较非五子点数的大小
            Map<Integer, Long> countMap1 = getCountMap(dice);
            
            int singlePoint1 = countMap1.entrySet().stream()
                    .filter(e -> e.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);

            return DiceOutcome.WU_ZI.rank + singlePoint1;
        }
    },
    ZHUANG_YUAN("普通状元", 60) {
        @Override
        public boolean matches(List<Integer> dice) {
            return getCountMap(dice).getOrDefault(4, 0L) == 4;
        }
        
        @Override
        public int calScore(List<Integer> dice) {
            int sum1 = dice.stream().filter(d -> d != 4).mapToInt(Integer::intValue).sum();
            return DiceOutcome.ZHUANG_YUAN.rank + sum1;
        }
    },
    DUI_TANG("对堂", 50) {
        @Override
        public boolean matches(List<Integer> dice) {
            List<Integer> sorted = dice.stream().sorted().toList();
            return sorted.equals(Arrays.asList(1,2,3,4,5,6));
        }
        
        @Override
        public int calScore(List<Integer> dice1) {
            // 对堂无需进一步比较
            return DiceOutcome.DUI_TANG.rank;
        }
    },
    SAN_HONG("三红", 40) {
        @Override
        public boolean matches(List<Integer> dice) {
            return getCountMap(dice).getOrDefault(4, 0L) == 3;
        }

        @Override
        public int calScore(List<Integer> dice) {
            return DiceOutcome.SAN_HONG.rank;
        }
    },
    SI_JIN("四进", 30) {
        @Override
        public boolean matches(List<Integer> dice) {
            Map<Integer, Long> countMap = getCountMap(dice);
            for (Map.Entry<Integer, Long> entry : countMap.entrySet()) {
                if (entry.getValue() == 4 && entry.getKey() != 4) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int calScore(List<Integer> dice) {
            // 四进无需进一步比较
            return DiceOutcome.SI_JIN.rank;
        }
    },
    ER_JU("二举", 20) {
        @Override
        public boolean matches(List<Integer> dice) {
            return getCountMap(dice).getOrDefault(4, 0L) == 2;
        }

        @Override
        public int calScore(List<Integer> dice) {
            // 二举无需进一步比较
            return DiceOutcome.ER_JU.rank;
        }
    },
    YI_XIU("一秀", 10) {
        @Override
        public boolean matches(List<Integer> dice) {
            return getCountMap(dice).getOrDefault(4, 0L) == 1;
        }

        @Override
        public int calScore(List<Integer> dice) {
            // 一秀无需进一步比较
            return DiceOutcome.YI_XIU.rank;
        }
    },
    PU_TONG("普通", 0) {
        @Override
        public boolean matches(List<Integer> dice) {
            return true;
        }

        @Override
        public int calScore(List<Integer> dice) {
            // 普通无需进一步比较
            return DiceOutcome.PU_TONG.rank;
        }
    };

    private final String name;
    private final int rank;

    DiceOutcome(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    /** 判定当前枚举是否匹配该骰子组合 */
    public abstract boolean matches(List<Integer> dice);
    
    /**
     * 计算单次博饼的得分
     */
    public abstract int calScore(List<Integer> dice);

    /**
     * 统计点数 -> 出现次数
     */
    protected static Map<Integer, Long> getCountMap(List<Integer> dice) {
        return dice.stream()
                .collect(Collectors.groupingBy(d -> d, Collectors.counting()));
    }

    /**
     * 根据骰子点数列表返回判定结果（优先级最高的先匹配）
     */
    public static DiceOutcome fromDice(List<Integer> dice) {
        return Arrays.stream(DiceOutcome.values())
                .filter(r -> r.matches(dice))
                .max(Comparator.comparingInt(DiceOutcome::getRank))
                .orElse(PU_TONG);
    }

    @Override
    public String toString() {
        return name;
    }

    @JsonValue // 序列化时只输出这个字段
    public String toValue() {
        return name;
    }

    @JsonCreator
    public static DiceOutcome fromValue(String value) {
        for (DiceOutcome outcome : values()) {
            if (outcome.name.equals(value)) {
                return outcome;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
