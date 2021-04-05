package com.github.remering.scratch.springboot.util.converter;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.core.convert.converter.Converter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


@MappedTypes(UUID.class)
@MappedJdbcTypes(JdbcType.BINARY)
public class UUIDMybatisTypeHandler extends BaseTypeHandler<UUID> {

    private final Converter<byte[], UUID> bytes2UUIDConverter = new Bytes2UUIDConverter();
    private final Converter<UUID, byte[]> uuid2BytesConverter = new UUID2BytesConverter();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
        ps.setBytes(i, uuid2BytesConverter.convert(parameter));
    }

    @Override
    public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return bytes2UUIDConverter.convert(rs.getBytes(columnName));
    }

    @Override
    public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return bytes2UUIDConverter.convert(rs.getBytes(columnIndex));
    }

    @Override
    public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return bytes2UUIDConverter.convert(cs.getBytes(columnIndex));
    }
}
