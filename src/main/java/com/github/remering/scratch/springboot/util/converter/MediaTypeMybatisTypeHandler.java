package com.github.remering.scratch.springboot.util.converter;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.http.MediaType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(MediaType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MediaTypeMybatisTypeHandler extends BaseTypeHandler<MediaType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, MediaType parameter, JdbcType jdbcType) throws SQLException {
            ps.setString(i, parameter.toString());
    }

    @Override
    public MediaType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return MediaType.parseMediaType(rs.getString(columnName));
    }

    @Override
    public MediaType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return MediaType.parseMediaType(rs.getString(columnIndex));
    }

    @Override
    public MediaType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return MediaType.parseMediaType(cs.getString(columnIndex));
    }
}
