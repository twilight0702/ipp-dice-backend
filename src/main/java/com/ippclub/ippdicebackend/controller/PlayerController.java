package com.ippclub.ippdicebackend.controller;

import com.ippclub.ippdicebackend.bo.RollResult;
import com.ippclub.ippdicebackend.common.Response;
import com.ippclub.ippdicebackend.service.RollService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * （测试接口）
     * 进行一次博饼，返回结果
     */
    @RequestMapping("/test/onceRoll")
    public Response<RollResult> onceRoll() {
        return Response.success(rollService.roll());
    }
}
