package com.ippclub.ippdicebackend.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ippclub.ippdicebackend.common.Response;
import com.ippclub.ippdicebackend.dto.CreateRoomDTO;
import com.ippclub.ippdicebackend.entity.Room;
import com.ippclub.ippdicebackend.mapper.RoomMapper;
import com.ippclub.ippdicebackend.vo.CreateRoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    public CreateRoomVO createRoom(CreateRoomDTO request) {
        // 创建房间，返回房间ID
        Room room = new Room();
        room.setName(request.getName());
        room.setRound(request.getRound());

        // 设置过期时间
        if (request.getTtl() > 0) {
            room.setExpireTime(LocalDateTime.now().plusMinutes(request.getTtl()));
        } else {
            // 如果ttl为-1或未设置，则设置为null表示永不过期
            room.setExpireTime(null);
        }

        // 保存房间信息
        roomMapper.insert(room);

        // 返回VO
        CreateRoomVO vo = new CreateRoomVO();
        vo.setRoomId(room.getId());

        // 返回房间ID
        return vo;
    }

    public void updateRound(Long roomId, Integer round) {
        LambdaUpdateWrapper<Room> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Room::getId, roomId)
                .set(Room::getRound, round);
        roomMapper.update(updateWrapper);
    }
}