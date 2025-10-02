package com.ippclub.ippdicebackend.controller;

import com.ippclub.ippdicebackend.bo.RoleType;
import com.ippclub.ippdicebackend.common.Response;
import com.ippclub.ippdicebackend.dto.CreateRoomDTO;
import com.ippclub.ippdicebackend.exception.ResourceNotFoundException;
import com.ippclub.ippdicebackend.service.RoomService;
import com.ippclub.ippdicebackend.vo.CreateRoomVO;
import com.ippclub.ippdicebackend.vo.RoomInfoVO;
import com.ippclub.ippdicebackend.vo.RoomListVO;
import com.ippclub.ippdicebackend.vo.RoomRankVO;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 博饼游戏房间控制接口
 */
@Slf4j
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

    /**
     * 获取一个房间的信息
     */
    @GetMapping("/info/{roomId}")
    public Response<RoomInfoVO> getRoomInfo(@PathVariable Long roomId) {
        return Response.success(roomService.getRoomInfo(roomId));
    }

    /**
     * 获取房间排行榜
     */
    @GetMapping("/rank")
    public Response<RoomRankVO> getRoomRank(@RequestParam("roomId") Long roomId, @RequestParam("roleType") int roleTypeId) {
        RoleType roleType;
        try{
            roleType = RoleType.fromId(roleTypeId);
            log.info("获取房间排行榜，roomId = {}，roleType = {}", roomId, roleType.getName());
        }
        catch(Exception e){
            throw new ResourceNotFoundException("无效的排序规则，roleTypeId = "+roleTypeId);
        }

        return Response.success(roomService.getRoomRank(roomId, roleType));
    }

    /**
     * 关闭房间
     */
    @PatchMapping("/close")
    public Response<?> closeRoom(@RequestParam("roomId") Long roomId) {
        roomService.closeRoom(roomId);
        return Response.success();
    }

    /**
     * 开启房间
     */
    @PatchMapping("/open")
    public Response<?> openRoom(@RequestParam("roomId") Long roomId) {
        roomService.openRoom(roomId);
        return Response.success();
    }

    /**
     * 列出当前所有房间信息
     */
    @GetMapping("/list")
    public Response<RoomListVO> listRooms() {
        return Response.success(roomService.listRooms());
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
