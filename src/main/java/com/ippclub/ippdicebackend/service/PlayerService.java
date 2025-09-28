package com.ippclub.ippdicebackend.service;

import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ippclub.ippdicebackend.bo.RollResult;
import com.ippclub.ippdicebackend.dto.JoinRoomDTO;
import com.ippclub.ippdicebackend.entity.Player;
import com.ippclub.ippdicebackend.entity.RollRecord;
import com.ippclub.ippdicebackend.entity.Room;
import com.ippclub.ippdicebackend.entity.RoomPlayer;
import com.ippclub.ippdicebackend.exception.BusinessConflictException;
import com.ippclub.ippdicebackend.mapper.PlayerMapper;
import com.ippclub.ippdicebackend.mapper.RollRecordMapper;
import com.ippclub.ippdicebackend.mapper.RoomMapper;
import com.ippclub.ippdicebackend.mapper.RoomPlayerMapper;
import com.ippclub.ippdicebackend.vo.PlayerRollVO;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlayerService {
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private RoomPlayerMapper roomPlayerMapper;
    @Autowired
    private RollRecordMapper rollRecordMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RollService rollService;

    public void joinRoom(JoinRoomDTO request) {
        log.info("玩家尝试加入房间，请求参数: {}", request);
        Player player = addPlayer(request);
        addRoomPlayer(request.getRoomId(), player.getId());
        log.info("玩家 {} 成功加入房间 {}", player.getName(), request.getRoomId());
    }

    public PlayerRollVO roll(JoinRoomDTO request){
        log.info("玩家在房间 {} 中进行投掷，请求参数: {}", request.getRoomId(), request);
        Player player = addPlayer(request);
        addRoomPlayer(request.getRoomId(), player.getId());
        RollRecord rollRecord = addPlayRecord(request.getRoomId(), player.getId());
        log.info("玩家 {} 在房间 {} 中完成投掷", player.getName(), request.getRoomId());

        PlayerRollVO playerRollVO = new PlayerRollVO();
        playerRollVO.setRound(rollRecord.getRound());
        playerRollVO.setDice(rollRecord.getDice());
        playerRollVO.setOutcome(rollRecord.getOutcome());
        playerRollVO.setScore(rollRecord.getScore());

        return playerRollVO;
    }

    /**
     * 添加玩家
     */
    public Player addPlayer(JoinRoomDTO request) {
        log.debug("开始添加玩家，学号: {}, 姓名: {}", request.getCardnum(), request.getName());
        Player player = new Player();
        player.setCardnum(request.getCardnum());
        player.setName(request.getName());

        LambdaQueryWrapper<Player> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Player::getCardnum, request.getCardnum());
        boolean exists = playerMapper.exists(queryWrapper);

        if (exists) {
            log.info("玩家已存在，学号: {}", request.getCardnum());
            Player existingPlayer = playerMapper.selectOne(queryWrapper);
            log.debug("返回已存在的玩家信息: {}", existingPlayer);
            return existingPlayer;
        } else {
            log.info("创建新玩家，学号: {}, 姓名: {}", request.getCardnum(), request.getName());
            int insertResult = playerMapper.insert(player);
            log.debug("玩家创建结果，影响行数: {}", insertResult);
        }

        return player;
    }

    /**
     * 在房间-玩家表中加入玩家
     */
    public void addRoomPlayer(Long roomId, Long playerId) {
        log.debug("检查玩家是否已加入房间，房间ID: {}, 玩家ID: {}", roomId, playerId);
        // 查看是否已经加入
        LambdaQueryWrapper<RoomPlayer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoomPlayer::getRoomId, roomId);
        queryWrapper.eq(RoomPlayer::getPlayerId, playerId);
        boolean exists = roomPlayerMapper.exists(queryWrapper);

        if (exists) {
            log.info("玩家已加入房间，无需重复加入，房间ID: {}, 玩家ID: {}", roomId, playerId);
            return;
        }

        log.debug("玩家未加入房间，正在添加，房间ID: {}, 玩家ID: {}", roomId, playerId);
        // 在房间-玩家表中加入玩家
        RoomPlayer roomPlayer = new RoomPlayer();
        roomPlayer.setRoomId(roomId);
        roomPlayer.setPlayerId(playerId);
        roomPlayer.setJoinTime(LocalDateTime.now());
        
        log.info("将玩家添加到房间-玩家表，房间ID: {}, 玩家ID: {}", roomId, playerId);
        int insertResult = roomPlayerMapper.insert(roomPlayer);
        log.debug("房间-玩家关系创建结果，影响行数: {}", insertResult);
    }

    /**
     * 增加游玩记录
     */
    public RollRecord addPlayRecord(Long roomId, Long playerId) {
        log.debug("开始增加游玩记录，房间ID: {}, 玩家ID: {}", roomId, playerId);
        LambdaQueryWrapper<RollRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RollRecord::getRoomId, roomId);
        queryWrapper.eq(RollRecord::getPlayerId, playerId);
        List<RollRecord> rollRecords = rollRecordMapper.selectList(queryWrapper);
        log.debug("已查询到玩家在房间中的投掷记录数量: {}", rollRecords.size());

        Room room = roomMapper.selectById(roomId);
        log.debug("查询到房间信息: {}", room);

        // 判断轮次是否合理
        if (rollRecords.size() >= room.getRound()) {
            log.warn("轮次已结束，无法继续投掷，房间ID: {}, 玩家ID: {}, 当前轮次: {}, 房间总轮次: {}", 
                     roomId, playerId, rollRecords.size(), room.getRound());
            throw new BusinessConflictException(1001, "轮次已结束，无法继续投掷");
        }

        log.info("执行投掷操作，房间ID: {}, 玩家ID: {}, 当前轮次: {}", roomId, playerId, rollRecords.size() + 1);
        RollResult roll = rollService.roll();
        log.debug("投掷结果: {}", roll);
        
        RollRecord rollRecord = new RollRecord();
        rollRecord.setRoomId(roomId);
        rollRecord.setPlayerId(playerId);
        rollRecord.setRound(rollRecords.size() + 1);
        rollRecord.setOutcome(String.valueOf(roll.getOutcome()));
        rollRecord.setDice(roll.getDices().toString());
        rollRecord.setScore(roll.getScore());
        rollRecord.setCreateTime(LocalDateTime.now());
        
        log.info("保存投掷记录，房间ID: {}, 玩家ID: {}, 轮次: {}, 结果: {}， 骰子: {}",
                 roomId, playerId, rollRecord.getRound(), rollRecord.getOutcome(), rollRecord.getDice());
        int insertResult = rollRecordMapper.insert(rollRecord);
        log.debug("投掷记录保存结果，影响行数: {}", insertResult);

        return rollRecord;
    }
}