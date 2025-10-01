package com.ippclub.ippdicebackend.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CreateRoomVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roomId;
}
