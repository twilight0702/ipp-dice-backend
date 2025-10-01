package com.ippclub.ippdicebackend.handler;

import com.ippclub.ippdicebackend.bo.DiceOutcome;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DiceOutcome枚举类型处理器
 * 用于处理数据库中存储的中文名称与枚举对象之间的转换
 */
public class DiceOutcomeTypeHandler extends BaseTypeHandler<DiceOutcome> {

    @Override
    public void setParameter(PreparedStatement ps, int i, DiceOutcome parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, jdbcType.TYPE_CODE);
        } else {
            ps.setString(i, parameter.getName());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DiceOutcome parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getName());
    }

    @Override
    public DiceOutcome getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : DiceOutcome.fromValue(value);
    }

    @Override
    public DiceOutcome getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : DiceOutcome.fromValue(value);
    }

    @Override
    public DiceOutcome getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : DiceOutcome.fromValue(value);
    }
}