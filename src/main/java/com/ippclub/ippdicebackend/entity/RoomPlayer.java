package com.ippclub.ippdicebackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 房间-玩家关联实体类
 * 对应数据库表: room_player
 */
@Data
@TableName("room_player")
public class RoomPlayer {

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
     * 加入时间
     */
    @TableField("join_time")
    private LocalDateTime joinTime;
}