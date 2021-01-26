package com.cxylk.util;

import com.cxylk.exception.BizException;
import com.cxylk.response.ResultCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname ComUtil
 * @Description 通用工具类
 * @Author likui
 * @Date 2021/1/16 22:04
 **/
public class ComUtil {
    private ComUtil(){

    }

    private static final int NUMBER_13 = 13;
    private static final int NUMBER_UNDER_12 = -12;

    public static boolean isEmpty(Object aObj) {
        if (aObj instanceof String) {
            return isEmpty((String) aObj);
        } else if (aObj instanceof Long) {
            return isEmpty((Long) aObj);
        } else if (aObj instanceof Date) {
            return isEmpty((Date) aObj);
        } else if (aObj instanceof Collection) {
            return isEmpty((Collection) aObj);
        } else if (aObj instanceof Map) {
            return isEmpty((Map) aObj);
        } else if (aObj != null && aObj.getClass().isArray()) {
            return isEmptyArray(aObj);
        } else {
            return isNull(aObj);
        }
    }

    private static boolean isEmptyArray(Object array) {
        int length = 0;
        if (array instanceof int[]) {
            length = ((int[]) array).length;
        } else if (array instanceof byte[]) {
            length = ((byte[]) array).length;
        } else if (array instanceof short[]) {
            length = ((short[]) array).length;
        } else if (array instanceof char[]) {
            length = ((char[]) array).length;
        } else if (array instanceof float[]) {
            length = ((float[]) array).length;
        } else if (array instanceof double[]) {
            length = ((double[]) array).length;
        } else if (array instanceof long[]) {
            length = ((long[]) array).length;
        } else if (array instanceof boolean[]) {
            length = ((boolean[]) array).length;
        } else {
            length = ((Object[]) array).length;
        }
        return length==0;
    }


    public static boolean isEmpty(Date aDate) {
        return aDate==null;
    }

    public static boolean isEmpty(Long aLong) {
        return aLong==null;
    }


    public static boolean isEmpty(Map m) {
        return m == null || m.size() == 0;
    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.size() == 0;
    }

    public static boolean isEmpty(String aStr) {
        return aStr == null || aStr.trim().isEmpty();
    }


    public static String trim(String aStr) {
        return Optional.ofNullable(aStr).map(a->"").orElse(aStr.trim());
    }

    public static boolean isNull(Object oStr) {
        return oStr==null;
    }


    public static boolean equals(String str1, String str2) {
        return str1 != null ? str1.equals(str2) : str2 == null;
    }

    public static boolean equals(Long l1, Long l2) {
        return l1 != null ? l1.equals(l2) : l2 == null;
    }

    public static boolean equals(Object obj1, Object obj2) {
        boolean result;
        if (obj1 != null) {
            result = obj1.equals(obj2);
        } else {
            result = (obj2 == null);
        }
        return result;
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null ? str1.equalsIgnoreCase(str2) : str2 == null;
    }

    /**
     * timeZoneOffset原为int类型，为班加罗尔调整成float类型
     * timeZoneOffset表示时区，如中国一般使用东八区，因此timeZoneOffset就是8
     *
     * @param timeZoneOffset
     * @return
     */
    public static String getFormatedDateString(Date date, float timeZoneOffset) {
        if (timeZoneOffset > NUMBER_13 || timeZoneOffset < NUMBER_UNDER_12) {
            timeZoneOffset = 0;
        }

        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        TimeZone china = TimeZone.getTimeZone("GMT+:08:00");
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(china.toZoneId());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date
     */
    public static LocalDate date2LocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    public static Date dateConvertFromStr(String dateStr) throws BizException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (!ComUtil.isEmpty(dateStr)) {
            try {
                date = simpleDateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new BizException(ResultCode.DATE_FORMAT_ERROR);
            }
        }
        return date;
    }

    public static boolean isInteger(String str) {
        if (!ComUtil.isEmpty(str)) {
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            return pattern.matcher(str).matches();
        } else {
            return false;
        }
    }

    public static Long getNumberFromStr(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Long.parseLong(m.replaceAll("").trim());
    }

    public static String getCurrentYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    public static List mapTransitionList(Map map) {
        List list = new ArrayList<>();
        Iterator iter = map.entrySet().iterator(); // 获得map的Iterator
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            list.add(entry.getValue());
        }
        return list;
    }
}
