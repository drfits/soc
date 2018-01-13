package com.drfits.soc.test.core.utils;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Compare the ISO date-time with an offset, such as '2011-12-03T10:15:30+01:00' and Calendar
 */
public final class StringToCalendarCompare {

    private final LocalDateTime dateTime;

    public StringToCalendarCompare(@Nonnull final String time) {
        this.dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public boolean same(@Nonnull final Calendar calendar) {
        return this.dateTime.compareTo(calendar.toInstant()
                .atZone(calendar.getTimeZone().toZoneId())
                .toLocalDateTime()) == 0;
    }

}
