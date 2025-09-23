package com.ippclub.ippdicebackend.bo;

import java.util.*;
import java.util.stream.Collectors;

public enum DiceOutcome {
    LIU_PAO_HONG("六抔红", 100) {
        @Override
        public boolean matches(List<Integer> dice) {
            return dice.stream().allMatch(d -> d == 4);
        }
        
        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 六抔红是最高级，无需比较
            return 0;
        }
    },
    ZHUANG_YUAN_CJH("状元插金花", 90) {
        @Override
        public boolean matches(List<Integer> dice) {
            Map<Integer, Long> countMap = getCountMap(dice);
            return countMap.getOrDefault(4, 0L) == 4 && countMap.getOrDefault(1, 0L) == 2;
        }
        
        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 状元插金花无需进一步比较（除非有多个，但规则中未说明）
            return 0;
        }
    },
    WU_HUANG("五红", 85) {
        @Override
        public boolean matches(List<Integer> dice) {
            Map<Integer, Long> countMap = getCountMap(dice);
            return countMap.getOrDefault(4, 0L) == 5;
        }
        
        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 比较非四点的点数大小
            int nonFour1 = dice1.stream().filter(d -> d != 4).findFirst().orElse(0);
            int nonFour2 = dice2.stream().filter(d -> d != 4).findFirst().orElse(0);
            return Integer.compare(nonFour1, nonFour2);
        }
    },
    WU_ZI("五子登科", 80) {
        @Override
        public boolean matches(List<Integer> dice) {
            Map<Integer, Long> countMap = getCountMap(dice);
            return countMap.values().stream().anyMatch(c -> c == 5) && countMap.getOrDefault(4, 0L) != 5;
        }
        
        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 比较非五子点数的大小
            Map<Integer, Long> countMap1 = getCountMap(dice1);
            Map<Integer, Long> countMap2 = getCountMap(dice2);
            
            int singlePoint1 = countMap1.entrySet().stream()
                    .filter(e -> e.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);
                    
            int singlePoint2 = countMap2.entrySet().stream()
                    .filter(e -> e.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0);
            
            return Integer.compare(singlePoint1, singlePoint2);
        }
    },
    ZHUANG_YUAN("普通状元", 75) {
        @Override
        public boolean matches(List<Integer> dice) {
            return getCountMap(dice).getOrDefault(4, 0L) == 4;
        }
        
        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 比较另外两个点数之和
            int sum1 = dice1.stream().filter(d -> d != 4).mapToInt(Integer::intValue).sum();
            int sum2 = dice2.stream().filter(d -> d != 4).mapToInt(Integer::intValue).sum();
            return Integer.compare(sum1, sum2);
        }
    },
    DUI_TANG("对堂", 70) {
        @Override
        public boolean matches(List<Integer> dice) {
            List<Integer> sorted = dice.stream().sorted().collect(Collectors.toList());
            return sorted.equals(Arrays.asList(1,2,3,4,5,6));
        }
        
        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 对堂无需进一步比较
            return 0;
        }
    },
    SAN_HONG("三红", 60) {
        @Override
        public boolean matches(List<Integer> dice) {
            return getCountMap(dice).getOrDefault(4, 0L) == 3;
        }

        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            return 0;
        }
    },
    SI_JIN("四进", 50) {
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
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 四进无需进一步比较
            return 0;
        }
    },
    ER_JU("二举", 40) {
        @Override
        public boolean matches(List<Integer> dice) {
            return getCountMap(dice).getOrDefault(4, 0L) == 2;
        }

        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 二举无需进一步比较
            return 0;
        }
    },
    YI_XIU("一秀", 30) {
        @Override
        public boolean matches(List<Integer> dice) {
            return getCountMap(dice).getOrDefault(4, 0L) == 1;
        }

        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 一秀无需进一步比较
            return 0;
        }
    },
    PU_TONG("普通", 0) {
        @Override
        public boolean matches(List<Integer> dice) {
            return true;
        }

        @Override
        public int compareSameLevel(List<Integer> dice1, List<Integer> dice2) {
            // 普通无需进一步比较
            return 0;
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
     * 比较同等级骰子结果的大小
     * @param dice1 第一个骰子组合
     * @param dice2 第二个骰子组合
     * @return 正数表示第一个更大，负数表示第二个更大，0表示相等
     */
    public abstract int compareSameLevel(List<Integer> dice1, List<Integer> dice2);

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
    
    /**
     * 根据 int[] 类型的骰子数组创建 DiceOutcome
     */
    public static DiceOutcome fromDice(int[] dice) {
        List<Integer> diceList = Arrays.stream(dice).boxed().collect(Collectors.toList());
        return fromDice(diceList);
    }
}
