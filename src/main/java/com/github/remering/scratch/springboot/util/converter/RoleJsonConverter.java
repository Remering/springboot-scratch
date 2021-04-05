package com.github.remering.scratch.springboot.util.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.github.remering.scratch.springboot.bean.Role;
import lombok.val;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class RoleJsonConverter {


    public static class RoleJsonDeserializer extends StdScalarDeserializer<Role> {

        public RoleJsonDeserializer() {
            super(Role.class);
        }

        @Override
        public Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            val ordinal = p.getValueAsInt();
            return Role.values()[ordinal];
        }

    }

    public static class RoleJsonSerializer extends StdScalarSerializer<Role> {

        public RoleJsonSerializer() {
            super(Role.class);
        }

        @Override
        public void serialize(Role value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value.ordinal());
        }
    }
}
