package com.ippclub.ippdicebackend.controller;

import com.ippclub.ippdicebackend.common.Response;
import com.ippclub.ippdicebackend.dto.CreateRoomDTO;
import com.ippclub.ippdicebackend.service.RoomService;
import com.ippclub.ippdicebackend.vo.CreateRoomVO;
import jakarta.websocket.server.PathParam;
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

    /**
     * 创造房间
     * @param request
     * @return
     */
    @PostMapping("/create")
    public Response<CreateRoomVO> createRoom(@RequestBody CreateRoomDTO request) {
        return Response.success("创建房间成功",roomService.createRoom(request));
    }

    /**
     * 修改轮次
     */
    @PatchMapping("/round")
    public Response<?> updateRound(@RequestParam("roomId") Long roomId, @RequestParam("round") Integer round) {
        roomService.updateRound(roomId, round);
        return Response.success();
    }

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
