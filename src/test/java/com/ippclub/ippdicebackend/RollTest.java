package com.ippclub.ippdicebackend;

import com.ippclub.ippdicebackend.bo.RoleType;
import com.ippclub.ippdicebackend.entity.Player;
import com.ippclub.ippdicebackend.entity.Room;
import com.ippclub.ippdicebackend.entity.RoomPlayer;
import com.ippclub.ippdicebackend.entity.RollRecord;
import com.ippclub.ippdicebackend.mapper.PlayerMapper;
import com.ippclub.ippdicebackend.mapper.RoomMapper;
import com.ippclub.ippdicebackend.mapper.RoomPlayerMapper;
import com.ippclub.ippdicebackend.mapper.RollRecordMapper;
import com.ippclub.ippdicebackend.service.RollService;
import com.ippclub.ippdicebackend.service.RoomService;
import com.ippclub.ippdicebackend.vo.PlayerRecordVO;
import com.ippclub.ippdicebackend.vo.RoomRankVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RollTest {
    @Autowired
    private RollService rollService;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoomPlayerMapper roomPlayerMapper;

    @Autowired
    private RollRecordMapper rollRecordMapper;
    @Autowired
    private RoomService roomService;

    @Test
    public void testRoll() {
        for (int i = 0; i < 10; i++) {
            System.out.println(rollService.roll());
        }
    }

    @Test
    public void testInsert() {
        // 插入一个player模拟数据
        Player player = new Player();
        player.setCardnum("123456789012345678");
        player.setName("test");
        playerMapper.insert(player);
    }

    /**
     * 测试所有表的数据插入功能
     * 验证Player、Room、RoomPlayer和RollRecord实体的插入操作
     */
    @Test
    public void testInsertAllTables() {
        // 插入玩家数据
        Player player = new Player();
        player.setCardnum("123456789012345678");
        player.setName("测试玩家");
        playerMapper.insert(player);
        System.out.println("插入玩家数据成功，ID: " + player.getId());

        // 插入房间数据
        Room room = new Room();
        room.setName("测试房间");
        room.setExpireTime(LocalDateTime.now().plusHours(1)); // 1小时后过期
        roomMapper.insert(room);
        System.out.println("插入房间数据成功，ID: " + room.getId());

        // 插入房间玩家关联数据
        RoomPlayer roomPlayer = new RoomPlayer();
        roomPlayer.setRoomId(room.getId());
        roomPlayer.setPlayerId(player.getId());
        roomPlayerMapper.insert(roomPlayer);
        System.out.println("插入房间玩家关联数据成功，ID: " + roomPlayer.getId());

        // 插入投掷记录数据
        RollRecord rollRecord = new RollRecord();
        rollRecord.setRoomId(room.getId());
        rollRecord.setPlayerId(player.getId());
        rollRecord.setRound(1);
        rollRecord.setDice("[1,2,3,4,5,6]"); // 模拟骰子结果
        rollRecord.setOutcome("测试结果");
        rollRecord.setScore(100);
        rollRecordMapper.insert(rollRecord);
        System.out.println("插入投掷记录数据成功，ID: " + rollRecord.getId());

        System.out.println("所有表数据插入测试完成");
    }

    @Test
    public void test3() {
        Long roomId = 1973260981480943617L;
        List<PlayerRecordVO> playerRecordVOS = rollRecordMapper.selectPlayerRecordsByRoomId(roomId);
        System.out.println(playerRecordVOS);
    }

    @Test
    public void test4() {
        Long roomId = 1973260981480943617L;
        RoomRankVO roomRank = roomService.getRoomRank(roomId, RoleType.HIGHEST_SCORE);
        System.out.println(roomRank);
        roomRank = roomService.getRoomRank(roomId, RoleType.LAST_ROLL);
        System.out.println(roomRank);
        roomRank = roomService.getRoomRank(roomId, RoleType.TOTAL_SCORE);
        System.out.println(roomRank);
    }
}