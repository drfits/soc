package com.drfits.soc.test.core.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class StringToCalendarCompareTest {

    private final static String TEST_TIME = "2017-11-01T09:40:02.000+03:00";
    private final static String TEST_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public StringToCalendarCompareTest() {
    }

    @Test
    public void same() throws ParseException {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TEST_TIME_FORMAT);
        calendar.setTime(simpleDateFormat.parse(TEST_TIME));
        new StringToCalendarCompare(TEST_TIME).same(calendar);
    }
}