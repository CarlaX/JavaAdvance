package com.fzw.threaddemo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author fzw
 * @description
 * @date 2021-05-23
 **/
public class TimeUtil {
    public static final String DEFAULT_ZONE_ID = "Asia/Shanghai";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final ZoneId DEFAULT_ZONE = ZoneId.of(DEFAULT_ZONE_ID);
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN).withZone(DEFAULT_ZONE);

    public static String currentDateTimeFormat() {
        return LocalDateTime.now().atZone(DEFAULT_ZONE).format(DEFAULT_DATETIME_FORMATTER);
    }
}
