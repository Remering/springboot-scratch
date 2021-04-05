package com.github.remering.scratch.springboot.util.converter;

import com.github.remering.scratch.springboot.bean.Role;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;


@MappedJdbcTypes(JdbcType.BINARY)
@MappedTypes(Role.class)
public class RoleMybatisTypeHandler extends EnumOrdinalTypeHandler<Role> {
    public RoleMybatisTypeHandler() {
        super(Role.class);
    }
}