package com.tmall.pokemon.bulbasaur.schedule.job;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: guichen - anson
 * Date: 12-4-9
 */
public class DayUtils {
    private static final String[] specialDay = new String[]{
            "1.1-1.3, 1.22-1.28, 4.2-4.4, 4.29-5.1, 6.22-6.24, 9.30-10.7; 1.21, 1.29, 3.31, 4.1, 4.28, 9.29", // 2012
            "1.1-1.3, 2.9-2.15, 4.4-4.6, 4.29-5.1, 6.10-6.12, 9.19-9.21, 10.1-10.7; 1.5-1.6, 2.16-2.17, 4.7, 4.27-4.28, 6.8-6.9, 9.22, 9.29, 10.12" // 2013
    };
    public static boolean initialized = false;
    private static byte[][] days;
    private static int baseYear = 2012;

    public static boolean isRestDay(Calendar cal) {
        if (!initialized) {
            throw new IllegalStateException("need init first");
        }
        int yearIndex = cal.get(Calendar.YEAR) - baseYear;
        if (yearIndex < 0 || yearIndex >= days.length) {
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
        }
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR) - 1;
        return (days[yearIndex][dayOfYear / 8] & (byte) (1 << dayOfYear % 8)) != 0;
    }

    public static long workTime(Calendar _calStart, Calendar _calEnd) {
        if (!initialized) {
            throw new IllegalStateException("need init first");
        }
        Calendar calStart = (Calendar) _calStart.clone();
        Calendar calEnd = (Calendar) _calEnd.clone();
        // 如果开始时间是个休息日 那就从下一个工作日的0毫秒算起
        if (isRestDay(calStart)) {
            do {
                calStart.add(Calendar.DAY_OF_YEAR, 1);
            } while (isRestDay(calStart));
            setDayBegin(calStart);
        }
        // 如果结束时间是个休息日 那就从上一个工作日的最后1毫秒算起
        if (isRestDay(calEnd)) {
            do {
                calEnd.add(Calendar.DAY_OF_YEAR, -1);
            } while (isRestDay(calEnd));
            setDayEnd(calEnd);
        }
        long workTime = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
        if (workTime <= 0) return 0;

        int restDayCount = 0;
        while (calStart.before(calEnd)) {
            if (isRestDay(calStart)) restDayCount++;
            calStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return workTime - (restDayCount * 24 * 60 * 60 * 1000L);
    }

    public static void init() {
        init(specialDay);
    }

    public static synchronized void init(String[] specialDay) {
        byte[][] tempDays = new byte[specialDay.length][46];

        for (int i = 0; i < specialDay.length; i++) {
            int year = baseYear + i;
            String[] specialDays = specialDay[i].split(";");
            String restDay = specialDays[0].trim();
            String workDay = specialDays[1].trim();

            Calendar cal = Calendar.getInstance();
            cal.set(year, Calendar.JANUARY, 1);
            int dayCount = 0;
            while (cal.get(Calendar.YEAR) < year + 1) {
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    setDay(tempDays, year, dayCount, true);
                }
                cal.add(Calendar.DAY_OF_YEAR, 1);
                dayCount++;
            }
            setDayWithString(tempDays, year, restDay, true);
            setDayWithString(tempDays, year, workDay, false);
        }
        days = tempDays;
        initialized = true;
    }

    private static void setDayWithString(byte[][] days, int year, String dayStr, boolean rest) {
        for (String dayRange : dayStr.split(",")) {
            String[] daysStr = dayRange.trim().split("-");
            if (daysStr.length == 1) {
                int month = Integer.parseInt(daysStr[0].split("\\.")[0].trim()) - 1;
                int day = Integer.parseInt(daysStr[0].split("\\.")[1].trim());
                setDay(days, year, month, day, rest);
            } else {
                int monthStart = Integer.parseInt(daysStr[0].split("\\.")[0].trim()) - 1;
                int dayStart = Integer.parseInt(daysStr[0].split("\\.")[1].trim());
                int monthEnd = Integer.parseInt(daysStr[1].split("\\.")[0].trim()) - 1;
                int dayEnd = Integer.parseInt(daysStr[1].split("\\.")[1].trim());
                setDayRange(days, year, monthStart, dayStart, monthEnd, dayEnd, rest);
            }
        }
    }

    private static void setDay(byte[][] days, int year, int dayOfYear, boolean rest) {
        int yearIndex = year - baseYear;
        if (rest) {
            days[yearIndex][dayOfYear / 8] |= (byte) (1 << dayOfYear % 8);
        } else {
            days[yearIndex][dayOfYear / 8] &= ~(byte) (1 << dayOfYear % 8);
        }
    }

    private static void setDay(byte[][] days, int year, int month, int day, boolean rest) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        setDay(days, year, cal.get(Calendar.DAY_OF_YEAR) - 1, rest);
    }

    private static void setDayRange(byte[][] days, int year, int dayOfYearStart, int dayOfYearEnd, boolean rest) {
        int dayOfYear = dayOfYearStart;
        while (dayOfYear <= dayOfYearEnd) {
            setDay(days, year, dayOfYear, rest);
            dayOfYear++;
        }
    }

    private static void setDayRange(byte[][] days, int year, int monthStart, int dayStart, int monthEnd, int dayEnd, boolean rest) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthStart, dayStart);
        int dayOfYearStart = cal.get(Calendar.DAY_OF_YEAR) - 1;
        cal.set(year, monthEnd, dayEnd);
        setDayRange(days, year, dayOfYearStart, cal.get(Calendar.DAY_OF_YEAR) - 1, rest);
    }

    private static void setDayBegin(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private static void setDayEnd(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }
}

