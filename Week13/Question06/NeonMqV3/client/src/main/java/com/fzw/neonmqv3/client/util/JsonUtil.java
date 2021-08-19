package com.fzw.neonmqv3.client.util;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER;
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(8);
    public static final ZoneId DEFAULT_GMT_ZONE = ZoneId.ofOffset("GMT", ZONE_OFFSET);

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone(DEFAULT_GMT_ZONE));
        OBJECT_MAPPER.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY);
    }

    public static <T> String jsonSerialize(T t) {
        String value = null;
        try {
            value = OBJECT_MAPPER.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static <T> T jsonDeserialize(String content, Class<T> clazz) {
        if (!JsonUtil.jsonDeserializeCheck(content, clazz)) {
            return null;
        }
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T jsonDeserializeWithType(String content, TypeReference<T> typeReference) {
        if (!JsonUtil.jsonDeserializeCheck(content, typeReference)) {
            return null;
        }
        T readValue = null;
        try {
            readValue = OBJECT_MAPPER.readValue(content, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readValue;
    }

    private static <T> boolean jsonDeserializeCheck(String content, Object type) {
        return content != null && type != null;
    }
}
