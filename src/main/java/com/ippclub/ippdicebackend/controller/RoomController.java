package com.ippclub.ippdicebackend.controller;

import com.ippclub.ippdicebackend.common.Response;
import com.ippclub.ippdicebackend.dto.CreateRoomDTO;
import com.ippclub.ippdicebackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 博饼游戏房间控制接口
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping("/create")
    public Response<?> createRoom(@RequestBody CreateRoomDTO request) {
        return roomService.createRoom(request);
    }
//
//    @PostMapping("/{roomId}/open")
//    public Response<?> openRoom(@PathVariable Long roomId) {
//        return roomService.openRoom(roomId);
//    }
//
//    @PostMapping("/{roomId}/close")
//    public Response<?> closeRoom(@PathVariable Long roomId) {
//        return roomService.closeRoom(roomId);
//    }
//
//    @GetMapping("/{roomId}/status")
//    public Response<?> getRoomStatus(@PathVariable Long roomId) {
//        return roomService.getRoomStatus(roomId);
//    }
//
//    @PostMapping("/{roomId}/next-round")
//    public Response<?> nextRound(@PathVariable Long roomId) {
//        return roomService.nextRound(roomId);
//    }
}
