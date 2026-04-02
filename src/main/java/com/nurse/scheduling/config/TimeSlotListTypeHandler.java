package com.nurse.scheduling.config;

import cn.hutool.json.JSONUtil;
import com.nurse.scheduling.dto.shift.TimeSlotDTO;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 时间段列表类型处理器
 * 用于将 List<TimeSlotDTO> 与数据库 JSON 字符串互相转换
 *
 * @author nurse-scheduling
 */
@MappedTypes({List.class})
public class TimeSlotListTypeHandler extends BaseTypeHandler<List<TimeSlotDTO>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<TimeSlotDTO> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSONUtil.toJsonStr(parameter));
    }

    @Override
    public List<TimeSlotDTO> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }

    @Override
    public List<TimeSlotDTO> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }

    @Override
    public List<TimeSlotDTO> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }

    private List<TimeSlotDTO> parseJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return JSONUtil.toList(json, TimeSlotDTO.class);
    }
}
