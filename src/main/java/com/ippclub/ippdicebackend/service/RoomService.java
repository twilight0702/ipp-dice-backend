package com.ippclub.ippdicebackend.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ippclub.ippdicebackend.bo.RoleType;
import com.ippclub.ippdicebackend.common.Response;
import com.ippclub.ippdicebackend.dto.CreateRoomDTO;
import com.ippclub.ippdicebackend.entity.RollRecord;
import com.ippclub.ippdicebackend.entity.Room;
import com.ippclub.ippdicebackend.exception.ResourceNotFoundException;
import com.ippclub.ippdicebackend.mapper.RollRecordMapper;
import com.ippclub.ippdicebackend.mapper.RoomMapper;
import com.ippclub.ippdicebackend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RollRecordMapper rollRecordMapper;

    @Autowired
    private TaskScheduler taskScheduler;

    // 存储房间ID与定时任务的映射关系
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    public CreateRoomVO createRoom(CreateRoomDTO request) {
        // 创建房间，返回房间ID
        Room room = new Room();
        room.setName(request.getName());
        room.setRound(request.getRound());

        // 设置过期时间
        if (request.getTtl() > 0) {
            room.setExpireTime(LocalDateTime.now().plusSeconds(request.getTtl()));
        } else {
            // 如果ttl为-1或未设置，则设置为null表示永不过期
            room.setExpireTime(null);
        }

        // 保存房间信息
        roomMapper.insert(room);

        // 如果设置了过期时间，则创建定时任务
        if (room.getExpireTime() != null) {
            scheduleRoomClosure(room.getId(), room.getExpireTime());
        }

        // 返回VO
        CreateRoomVO vo = new CreateRoomVO();
        vo.setRoomId(room.getId());

        // 返回房间ID
        return vo;
    }

    /**
     * 安排房间关闭任务
     * @param roomId 房间ID
     * @param expireTime 过期时间
     */
    private void scheduleRoomClosure(Long roomId, LocalDateTime expireTime) {
        // 取消已存在的任务（如果有的话）
        ScheduledFuture<?> existingTask = scheduledTasks.get(roomId);
        if (existingTask != null && !existingTask.isDone()) {
            existingTask.cancel(false);
        }

        // 创建新的定时任务
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                () -> closeRoom(roomId),
                Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant())
        );

        // 保存任务引用
        scheduledTasks.put(roomId, scheduledTask);
    }

    public void updateRound(Long roomId, Integer round) {
        LambdaUpdateWrapper<Room> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Room::getId, roomId)
                .set(Room::getRound, round);
        roomMapper.update(updateWrapper);
    }

    /**
     * 获取一个房间的信息
     */
    public RoomInfoVO getRoomInfo(Long roomId) {
        Optional<Room> optionalRoom = Optional.ofNullable(roomMapper.selectById(roomId));

        // 房间不存在
        if (optionalRoom.isEmpty()) {
            throw new ResourceNotFoundException("访问的房间不存在，roomId = " + roomId);
        }

        Room room = optionalRoom.get();
        return room2RoomInfoVO(room);
    }

    private RoomInfoVO room2RoomInfoVO(Room room){
        RoomInfoVO vo = new RoomInfoVO();
        vo.setId(room.getId());
        vo.setName(room.getName());
        vo.setExpireTime(room.getExpireTime());
        vo.setRound(room.getRound());
        vo.setIsOpen(room.getIsOpen());
        vo.setIsDel(room.getIsDel());
        vo.setCreateTime(room.getCreateTime());
        vo.setUpdateTime(room.getUpdateTime());
        return vo;
    }

    /**
     * 获取房间排行榜
     */
    public RoomRankVO getRoomRank(Long roomId, RoleType roleType) {
        List<PlayerRecordVO> roomRolls = getRoomRolls(roomId);
        if (roomRolls.isEmpty()){
            RoomRankVO vo = new RoomRankVO();
            vo.setPlayerRecords(new ArrayList<>());
            return vo;
        }

        List<PlayerRecordVO> records = switch (roleType) {
            case HIGHEST_SCORE -> getHighestScoreRank(roomRolls);
            case LAST_ROLL -> getLastRollRank(roomRolls);
            case TOTAL_SCORE -> getTotalScoreRank(roomRolls);
            default -> getHighestScoreRank(roomRolls); // 默认按最高分
        };

        RoomRankVO roomRankVO = new RoomRankVO();
        roomRankVO.setPlayerRecords(records);
        return roomRankVO;
    }

    private List<PlayerRecordVO> getHighestScoreRank(List<PlayerRecordVO> allRecords) {
        // 按玩家分组，取每个玩家的最高分记录
        Map<Long, PlayerRecordVO> highestScoreMap = allRecords.stream()
                .collect(Collectors.groupingBy(
                        PlayerRecordVO::getPlayerId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .max(Comparator.comparing(PlayerRecordVO::getScore)
                                                .thenComparing(PlayerRecordVO::getRollTime))
                                        .orElse(null)
                        )
                ));

        // 返回按分数从大到小排序的列表，分数相同时按时间从早到晚排序
        return highestScoreMap.values().stream()
                .filter(record -> record != null)
                .sorted(Comparator.comparing(PlayerRecordVO::getScore, Comparator.reverseOrder())
                        .thenComparing(PlayerRecordVO::getRollTime))
                .collect(Collectors.toList());
    }

    private List<PlayerRecordVO> getLastRollRank(List<PlayerRecordVO> allRecords) {
        // 按玩家分组，取每个玩家的最后一条记录
        Map<Long, PlayerRecordVO> lastRollMap = allRecords.stream()
                .collect(Collectors.groupingBy(
                        PlayerRecordVO::getPlayerId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .max(Comparator.comparing(PlayerRecordVO::getRollTime))
                                        .orElse(null)
                        )
                ));

        // 返回按分数从大到小排序的列表，分数相同时按时间从早到晚排序
        return lastRollMap.values().stream()
                .filter(record -> record != null)
                .sorted(Comparator.comparing(PlayerRecordVO::getScore, Comparator.reverseOrder())
                        .thenComparing(PlayerRecordVO::getRollTime))
                .collect(Collectors.toList());
    }

    private List<PlayerRecordVO> getTotalScoreRank(List<PlayerRecordVO> allRecords) {
        // 按玩家分组，计算每个玩家的总分
        Map<Long, PlayerRecordVO> totalScoreMap = allRecords.stream()
                .collect(Collectors.groupingBy(
                        PlayerRecordVO::getPlayerId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    // 创建一个新的PlayerRecordVO来存储总分信息
                                    PlayerRecordVO totalRecord = new PlayerRecordVO();
                                    totalRecord.setPlayerId(list.get(0).getPlayerId());
                                    totalRecord.setCardnum(list.get(0).getCardnum());
                                    totalRecord.setName(list.get(0).getName());

                                    // 计算总分
                                    int totalScore = list.stream()
                                            .mapToInt(PlayerRecordVO::getScore)
                                            .sum();
                                    totalRecord.setScore(totalScore);

                                    // 设置最后一次投掷的时间
                                    LocalDateTime lastRollTime = list.stream()
                                            .map(PlayerRecordVO::getRollTime)
                                            .max(LocalDateTime::compareTo)
                                            .orElse(null);
                                    totalRecord.setRollTime(lastRollTime);

                                    // 设置轮数为总记录数
                                    totalRecord.setRound(list.size());

                                    return totalRecord;
                                }
                        )
                ));

        // 返回按分数从大到小排序的列表，分数相同时按时间从早到晚排序
        return totalScoreMap.values().stream()
                .filter(record -> record != null)
                .sorted(Comparator.comparing(PlayerRecordVO::getScore, Comparator.reverseOrder())
                        .thenComparing(PlayerRecordVO::getRollTime))
                .collect(Collectors.toList());
    }

    /**
     * 获取某个房间的所有投掷信息
     */
    public List<PlayerRecordVO> getRoomRolls(Long roomId) {
        return rollRecordMapper.selectPlayerRecordsByRoomId(roomId);
    }

    /**
     * 关闭房间
     *
     * @param roomId
     * @return
     */
    public void closeRoom(Long roomId) {
        // 取消对应的定时任务（如果有的话）
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(roomId);
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(false);
            scheduledTasks.remove(roomId);
        }
        
        LambdaUpdateWrapper<Room> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Room::getId, roomId)
                .set(Room::getIsOpen, 0);
        int update = roomMapper.update(null, updateWrapper);
        if (update <= 0) {
            throw new ResourceNotFoundException("房间不存在， roomId = " + roomId);
        }
    }

    /**
     * 开启房间
     */
    public void openRoom(Long roomId) {
        LambdaUpdateWrapper<Room> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Room::getId, roomId)
                .set(Room::getIsOpen, 1);
        int update = roomMapper.update(null, updateWrapper);
        if (update <= 0) {
            throw new ResourceNotFoundException("房间不存在， roomId = " + roomId);
        }
    }

    public RoomListVO listRooms() {
        List<Room> rooms = roomMapper.selectList(null);
        List<RoomInfoVO> roomInfoVOS = new ArrayList<>();
        for (Room room : rooms) {
            roomInfoVOS.add( room2RoomInfoVO(room));
        }

        RoomListVO roomListVO = new RoomListVO();
        roomListVO.setRoomInfoVOS(roomInfoVOS);
        return roomListVO;
    }
}