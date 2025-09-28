package com.ippclub.ippdicebackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 玩家实体类
 * 对应数据库表: player
 */
@Data
@TableName("player")
public class Player {

    /**
     * 主键，雪花算法生成
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学号，唯一约束
     */
    @TableField("cardnum")
    private String cardnum;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}