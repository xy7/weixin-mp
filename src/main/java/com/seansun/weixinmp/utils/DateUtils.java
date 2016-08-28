package com.seansun.weixinmp.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT = "yyyyMMdd";

	public static int date2int(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		int dateInt = Integer.parseInt(sdf2.format(sdf.parse(date)));
		return dateInt;
	}
	
	public static String parseTimestamp(Timestamp timestamp, String pattern) {
        try {
            return new SimpleDateFormat(pattern).format(new Date(timestamp.getTime()));
        } catch (Exception e) {
            LOGGER.warn(String.format("Parse timestamp error! timestamp={}, pattern={}", timestamp, pattern), e);
            return new SimpleDateFormat(DEFAULT_DATETIME_PATTERN).format(new Date(timestamp.getTime()));
        }
    }

    public static Timestamp convertString2TimeStamp(String dateString, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (Exception ex) {
            LOGGER.warn(String.format("Parse data error! dateString={}", dateString), ex);
        }
        Timestamp ts = new Timestamp(date.getTime());
        return ts;
    }
}
