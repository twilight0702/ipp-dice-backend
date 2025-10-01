package com.ippclub.ippdicebackend.bo;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 排行榜生成规则
 */
public enum RoleType {
    /**
     * 按最高分
     */
    HIGHEST_SCORE("按最高分",1),
    /**
     * 按最后一次
     */
    LAST_ROLL("按最后一次",2),
    /**
     * 按总分
     */
    TOTAL_SCORE("按总分",3);

    private final String name;
    private final int id;

    RoleType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    /**
     * 根据id反序列化
     */
    @JsonCreator
    public static RoleType fromId(int id) {
        for (RoleType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown id: " + id);
    }
}
