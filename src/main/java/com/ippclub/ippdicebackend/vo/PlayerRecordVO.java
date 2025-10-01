package com.ippclub.ippdicebackend.vo;

import com.ippclub.ippdicebackend.bo.DiceOutcome;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlayerRecordVO {
    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 玩家学号
     */
    private String cardnum;

    /**
     * 玩家姓名
     */
    private String name;

    /**
     * 所属轮数
     */
    private Integer round;

    /**
     * 玩家骰子结果
     */
    private String dice;

    /**
     * 抽奖结果
     */
    private DiceOutcome diceOutcome;

    /**
     * 玩家得分
     */
    private Integer score;

    /**
     * 抽奖时间
     */
    private LocalDateTime rollTime;
}