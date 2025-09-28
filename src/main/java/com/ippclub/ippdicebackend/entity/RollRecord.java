package com.ippclub.ippdicebackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 投掷记录实体类
 * 对应数据库表: roll_record
 */
@Data
@TableName("roll_record")
public class RollRecord {

    /**
     * 主键，雪花算法生成
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 房间ID
     */
    @TableField("room_id")
    private Long roomId;

    /**
     * 玩家ID
     */
    @TableField("player_id")
    private Long playerId;

    /**
     * 第几轮
     */
    @TableField("round")
    private Integer round;

    /**
     * 骰子结果（JSON数组，例如 [1,4,6,3,2,5]）
     */
    @TableField("dice")
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

    /**
     * 投掷时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}