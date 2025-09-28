package com.ippclub.ippdicebackend.bo;

import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * 玩家单次博饼结果（值对象，不可变）
 */
@Getter
@ToString
public final class RollResult {

    /**
     * 投掷点数（1-6），固定长度6，不可变
     */
    private final List<Integer> dices;

    /**
     * 投掷结果
     */
    private final DiceOutcome outcome;

    /**
     * 单次投掷得分（用于排名）
     */
    private final Integer score;

    private RollResult(List<Integer> dices, DiceOutcome outcome, Integer score) {
        // 不可变List，防止外部修改
        this.dices = List.copyOf(dices);
        this.outcome = Objects.requireNonNull(outcome);
        this.score = score;
    }

    /**
     * 静态工厂方法：创建博饼结果
     */
    public static RollResult of(List<Integer> dices, DiceOutcome outcome, Integer score) {
        if (dices == null || dices.size() != 6) {
            throw new IllegalArgumentException("博饼必须正好有6个骰子");
        }
        return new RollResult(dices, outcome, score);
    }

//    /**
//     * 方便调试打印
//     */
//    @Override
//    public String toString() {
//        return "RollResult{dices=" + dices + ", outcome=" + outcome.getName() + '}';
//    }
}

