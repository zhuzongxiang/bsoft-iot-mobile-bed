package com.bsoft.iot.bed.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zzx
 */
public class DateFormatUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String format(Date date) {
        return sdf.format(date == null ? 0 : date);
    }

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

    public static String formatYMD(Date date) {
        return sdf2.format(date == null ? 0 : date);
    }

    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatYYMMDD(Date date) {
        return sdf2.format(date == null ? 0 : date);
    }
}
