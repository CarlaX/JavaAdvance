package com.fzw.insertdemo.util;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author fzw
 * @description
 **/
public class TimeUtil {
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(8);
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final ZoneId DEFAULT_ZONE = ZoneId.ofOffset("UTC", ZONE_OFFSET);
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN).withZone(DEFAULT_ZONE);

    public static Timestamp localDateTime2SqlTimeStamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public static String localDateTimeFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DEFAULT_DATETIME_FORMATTER);
    }

    public static String durationFormat(Duration duration) {
        return duration.toDaysPart() + "天 " + duration.toHoursPart() + ":" + duration.toMinutesPart() + ":" + duration.toSecondsPart() + "." + duration
                .toMillisPart() + " 总计秒数：" + duration.toSeconds() + "秒";
    }

    public static LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now().atZone(DEFAULT_ZONE).toLocalDateTime();
    }


}
