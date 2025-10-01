package com.ippclub.ippdicebackend.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class PlayerRollVO {
    /**
     * 玩家ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long playerId;

    /**
     * 轮次
     */
    private Integer round;

    /**
     * 骰子结果
     */
    private String dice;

    /**
     * 投掷结果描述
     */
    private String outcome;

    /**
     * 分数
     */
    private Integer score;
}
