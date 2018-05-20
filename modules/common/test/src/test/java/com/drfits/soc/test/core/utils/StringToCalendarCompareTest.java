package com.drfits.soc.test.core.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static org.junit.Assert.assertTrue;

public final class StringToCalendarCompareTest {

    private final static String TEST_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    @Test
    public void same() throws ParseException {
        final Calendar calendar = Calendar.getInstance();
        final ZonedDateTime now = ZonedDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TEST_TIME_FORMAT);
        final String formatDateTime = now.format(formatter);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TEST_TIME_FORMAT);
        calendar.setTime(simpleDateFormat.parse(formatDateTime));
        assertTrue(new StringToCalendarCompare(formatDateTime).same(calendar));
    }
}