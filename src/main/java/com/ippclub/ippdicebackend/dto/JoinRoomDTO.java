package com.ippclub.ippdicebackend.dto;

import lombok.Data;

@Data
public class JoinRoomDTO {
    private Long roomId;
    private Long playerId;
    /**
     * 参与的学生的学号
     */
    private String cardnum;

    /**
     * 学生姓名
     */
    private String name;
}
