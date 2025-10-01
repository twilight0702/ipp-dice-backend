package com.ippclub.ippdicebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ippclub.ippdicebackend.entity.RollRecord;
import com.ippclub.ippdicebackend.vo.PlayerRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 投掷记录Mapper接口
 * 提供对roll_record表的基本操作
 */
public interface RollRecordMapper extends BaseMapper<RollRecord> {
    /**
     * 根据房间ID查询玩家记录列表
     * @param roomId 房间ID
     * @return 玩家记录列表
     */
    List<PlayerRecordVO> selectPlayerRecordsByRoomId(@Param("roomId") Long roomId);

    /**
     * 根据房间ID和玩家ID查询记录列表
     */
    List<PlayerRecordVO> selectPlayerRecordsByRoomIdAndPlayerId(@Param("roomId") Long roomId, @Param("playerId") Long playerId);
}