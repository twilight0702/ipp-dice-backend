package com.ippclub.ippdicebackend.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomInfoVO {

    /**
     * 主键，雪花算法生成
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 房间名
     */
    private String name;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 当前轮次
     */
    private Integer round;

    /**
     * 是否开启
     */
    private Integer isOpen;

    /**
     * 逻辑删除标记，0=正常 1=删除
     */
    private Integer isDel;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
