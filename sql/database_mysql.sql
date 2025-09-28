-- 建库（可根据需要修改库名和字符集）
CREATE DATABASE IF NOT EXISTS ipp_dice
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE ipp_dice;

-- 1. 玩家表
DROP TABLE IF EXISTS player;
CREATE TABLE player (
                        id BIGINT NOT NULL COMMENT '主键，雪花算法生成',
                        cardnum VARCHAR(32) NOT NULL COMMENT '学号，唯一约束',
                        name VARCHAR(32) NOT NULL COMMENT '姓名',
                        create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        PRIMARY KEY (id),
                        UNIQUE KEY uk_cardnum (cardnum)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='玩家表';

-- 2. 房间表
DROP TABLE IF EXISTS room;
CREATE TABLE room (
                      id BIGINT NOT NULL COMMENT '主键，雪花算法生成',
                      name VARCHAR(64) NOT NULL COMMENT '房间名',
                      expire_time TIMESTAMP NULL DEFAULT NULL COMMENT '过期时间',
                      round INT NOT NULL DEFAULT 0 COMMENT '当前轮次',
                      is_del TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记，0=正常 1=删除',
                      create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                      PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间表';

-- 3. 房间玩家关联表
DROP TABLE IF EXISTS room_player;
CREATE TABLE room_player (
                             id BIGINT NOT NULL COMMENT '主键，雪花算法生成',
                             room_id BIGINT NOT NULL COMMENT '房间ID',
                             player_id BIGINT NOT NULL COMMENT '玩家ID',
                             join_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
                             PRIMARY KEY (id),
                             KEY idx_room (room_id),
                             KEY idx_player (player_id),
                             CONSTRAINT fk_room_player_room FOREIGN KEY (room_id) REFERENCES room(id),
                             CONSTRAINT fk_room_player_player FOREIGN KEY (player_id) REFERENCES player(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间-玩家关联表';

-- 4. 投掷记录表
DROP TABLE IF EXISTS roll_record;
CREATE TABLE roll_record (
                             id BIGINT NOT NULL COMMENT '主键，雪花算法生成',
                             room_id BIGINT NOT NULL COMMENT '房间ID',
                             player_id BIGINT NOT NULL COMMENT '玩家ID',
                             round INT NOT NULL COMMENT '第几轮',
                             dice JSON NOT NULL COMMENT '骰子结果（JSON数组，例如 [1,4,6,3,2,5]）',
                             outcome VARCHAR(64) NOT NULL COMMENT '投掷结果描述',
                             score INT NOT NULL COMMENT '分数',
                             create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投掷时间',
                             PRIMARY KEY (id),
                             KEY idx_room_round (room_id, round),
                             KEY idx_player (player_id),
                             CONSTRAINT fk_roll_record_room FOREIGN KEY (room_id) REFERENCES room(id),
                             CONSTRAINT fk_roll_record_player FOREIGN KEY (player_id) REFERENCES player(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投掷记录表';
