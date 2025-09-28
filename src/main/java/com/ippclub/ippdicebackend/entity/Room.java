package com.ippclub.ippdicebackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 房间实体类
 * 对应数据库表: room
 */
@Data
@TableName("room")
public class Room {

    /**
     * 主键，雪花算法生成
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 房间名
     */
    @TableField("name")
    private String name;

    /**
     * 过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 当前轮次
     */
    @TableField("round")
    private Integer round;

    /**
     * 逻辑删除标记，0=正常 1=删除
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}