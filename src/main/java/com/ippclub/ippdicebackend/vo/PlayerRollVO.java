package com.ippclub.ippdicebackend.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class PlayerRollVO {
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
    @TableField("outcome")
    private String outcome;

    /**
     * 分数
     */
    @TableField("score")
    private Integer score;
}
