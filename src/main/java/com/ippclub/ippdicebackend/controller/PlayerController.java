package com.ippclub.ippdicebackend.controller;

import com.ippclub.ippdicebackend.bo.RollResult;
import com.ippclub.ippdicebackend.common.Response;
import com.ippclub.ippdicebackend.dto.JoinRoomDTO;
import com.ippclub.ippdicebackend.service.PlayerService;
import com.ippclub.ippdicebackend.service.RollService;
import com.ippclub.ippdicebackend.vo.PlayerRollVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 玩家博饼游戏相关接口
 */
@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private RollService rollService;

    @Autowired
    private PlayerService playerService;

    /**
     * （测试接口）
     * 进行一次博饼，返回结果
     */
    @RequestMapping("/test/onceRoll")
    public Response<RollResult> onceRoll() {
        return Response.success(rollService.roll());
    }

    /**
     * 加入房间
     */
    @PostMapping("/join")
    public Response<?> joinRoom(@RequestBody JoinRoomDTO request) {
        playerService.joinRoom(request);
        return Response.success();
    }

    /**
     * 投掷一次
     */
    @PostMapping("/roll")
    public Response<PlayerRollVO> roll(@RequestBody JoinRoomDTO request) {
        PlayerRollVO roll = playerService.roll(request);
        return Response.success(roll);
    }
}
