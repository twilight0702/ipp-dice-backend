package com.ippclub.ippdicebackend.dto;

import lombok.Data;

/**
 * 创建房间DTO
 * （字段待完善）
 */
@Data
public class CreateRoomDTO {
    /**
     * 房间有效时间
     */
    private Integer ttl = -1;

    /**
     * 投掷轮次
     */
    private Integer round = 1;

    /**
     * 房间名字
     */
    private String name;
}
