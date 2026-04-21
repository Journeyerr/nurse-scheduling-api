package com.nurse.scheduling.common;

import java.time.LocalDate;
import java.util.*;

/**
 * 中国法定假日常量
 * 包含休假日期和调休上班日期
 * 每年需根据国务院公告更新
 *
 * @author nurse-scheduling
 */
public class HolidayConstants {

    // ========== 2025年法定假日 ==========
    private static final String[] HOLIDAYS_2025 = {
            // 元旦
            "2025-01-01",
            // 春节
            "2025-01-28", "2025-01-29", "2025-01-30", "2025-01-31", "2025-02-01", "2025-02-02", "2025-02-03", "2025-02-04",
            // 清明节
            "2025-04-04", "2025-04-05", "2025-04-06",
            // 劳动节
            "2025-05-01", "2025-05-02", "2025-05-03", "2025-05-04", "2025-05-05",
            // 端午节
            "2025-05-31", "2025-06-01", "2025-06-02",
            // 中秋节+国庆节
            "2025-10-01", "2025-10-02", "2025-10-03", "2025-10-04", "2025-10-05", "2025-10-06", "2025-10-07", "2025-10-08"
    };

    // 2025年调休上班日
    private static final String[] WORKDAYS_2025 = {
            "2025-01-26", // 春节调休
            "2025-02-08", // 春节调休
            "2025-04-27", // 劳动节调休
            "2025-09-28", // 国庆节调休
            "2025-10-11"  // 国庆节调休
    };

    // ========== 2026年法定假日 ==========
    private static final String[] HOLIDAYS_2026 = {
        // 元旦
        "2026-01-01", "2026-01-02", "2026-01-03",
        // 春节
        "2026-02-16", "2026-02-17", "2026-02-18", "2026-02-19", "2026-02-20", "2026-02-21", "2026-02-22",
        // 清明节
        "2026-04-04", "2026-04-05", "2026-04-06",
        // 劳动节
        "2026-05-01", "2026-05-02", "2026-05-03", "2026-05-04", "2026-05-05",
        // 端午节
        "2026-06-19", "2026-06-20", "2026-06-21",
        // 中秋节
        "2026-09-25", "2026-09-26", "2026-09-27",
        // 国庆节
        "2026-10-01", "2026-10-02", "2026-10-03", "2026-10-04", "2026-10-05", "2026-10-06", "2026-10-07"
    };

    // 2026年调休上班日
    private static final String[] WORKDAYS_2026 = {
        // 元旦调休
        "2026-01-04",
        // 春节调休
        "2026-02-14", "2026-02-28",
        // 劳动节调休
        "2026-05-09",
        // 国庆节调休
        "2026-09-20", "2026-10-10"
    };

    // 假日名称映射
    private static final Map<String, String> HOLIDAY_NAME_MAP = new LinkedHashMap<>();

    static {
        // 2025年假日名称
        HOLIDAY_NAME_MAP.put("2025-01-01", "元旦");
        HOLIDAY_NAME_MAP.put("2025-01-28", "春节"); HOLIDAY_NAME_MAP.put("2025-01-29", "春节");
        HOLIDAY_NAME_MAP.put("2025-01-30", "春节"); HOLIDAY_NAME_MAP.put("2025-01-31", "春节");
        HOLIDAY_NAME_MAP.put("2025-02-01", "春节"); HOLIDAY_NAME_MAP.put("2025-02-02", "春节");
        HOLIDAY_NAME_MAP.put("2025-02-03", "春节"); HOLIDAY_NAME_MAP.put("2025-02-04", "春节");
        HOLIDAY_NAME_MAP.put("2025-04-04", "清明"); HOLIDAY_NAME_MAP.put("2025-04-05", "清明");
        HOLIDAY_NAME_MAP.put("2025-04-06", "清明");
        HOLIDAY_NAME_MAP.put("2025-05-01", "劳动节"); HOLIDAY_NAME_MAP.put("2025-05-02", "劳动节");
        HOLIDAY_NAME_MAP.put("2025-05-03", "劳动节"); HOLIDAY_NAME_MAP.put("2025-05-04", "劳动节");
        HOLIDAY_NAME_MAP.put("2025-05-05", "劳动节");
        HOLIDAY_NAME_MAP.put("2025-05-31", "端午"); HOLIDAY_NAME_MAP.put("2025-06-01", "端午");
        HOLIDAY_NAME_MAP.put("2025-06-02", "端午");
        HOLIDAY_NAME_MAP.put("2025-10-01", "国庆"); HOLIDAY_NAME_MAP.put("2025-10-02", "国庆");
        HOLIDAY_NAME_MAP.put("2025-10-03", "国庆"); HOLIDAY_NAME_MAP.put("2025-10-04", "中秋");
        HOLIDAY_NAME_MAP.put("2025-10-05", "国庆"); HOLIDAY_NAME_MAP.put("2025-10-06", "国庆");
        HOLIDAY_NAME_MAP.put("2025-10-07", "国庆"); HOLIDAY_NAME_MAP.put("2025-10-08", "国庆");
        // 2026年假日名称
        HOLIDAY_NAME_MAP.put("2026-01-01", "元旦"); HOLIDAY_NAME_MAP.put("2026-01-02", "元旦");
        HOLIDAY_NAME_MAP.put("2026-01-03", "元旦");
        HOLIDAY_NAME_MAP.put("2026-02-16", "春节"); HOLIDAY_NAME_MAP.put("2026-02-17", "春节");
        HOLIDAY_NAME_MAP.put("2026-02-18", "春节"); HOLIDAY_NAME_MAP.put("2026-02-19", "春节");
        HOLIDAY_NAME_MAP.put("2026-02-20", "春节"); HOLIDAY_NAME_MAP.put("2026-02-21", "春节");
        HOLIDAY_NAME_MAP.put("2026-02-22", "春节");
        HOLIDAY_NAME_MAP.put("2026-04-04", "清明"); HOLIDAY_NAME_MAP.put("2026-04-05", "清明");
        HOLIDAY_NAME_MAP.put("2026-04-06", "清明");
        HOLIDAY_NAME_MAP.put("2026-05-01", "劳动节"); HOLIDAY_NAME_MAP.put("2026-05-02", "劳动节");
        HOLIDAY_NAME_MAP.put("2026-05-03", "劳动节"); HOLIDAY_NAME_MAP.put("2026-05-04", "劳动节");
        HOLIDAY_NAME_MAP.put("2026-05-05", "劳动节");
        HOLIDAY_NAME_MAP.put("2026-06-19", "端午"); HOLIDAY_NAME_MAP.put("2026-06-20", "端午");
        HOLIDAY_NAME_MAP.put("2026-06-21", "端午");
        HOLIDAY_NAME_MAP.put("2026-09-25", "中秋"); HOLIDAY_NAME_MAP.put("2026-09-26", "中秋");
        HOLIDAY_NAME_MAP.put("2026-09-27", "中秋");
        HOLIDAY_NAME_MAP.put("2026-10-01", "国庆"); HOLIDAY_NAME_MAP.put("2026-10-02", "国庆");
        HOLIDAY_NAME_MAP.put("2026-10-03", "国庆"); HOLIDAY_NAME_MAP.put("2026-10-04", "国庆");
        HOLIDAY_NAME_MAP.put("2026-10-05", "国庆"); HOLIDAY_NAME_MAP.put("2026-10-06", "国庆");
        HOLIDAY_NAME_MAP.put("2026-10-07", "国庆");
    }

    // 构建Set用于快速查找
    private static final Set<String> HOLIDAY_SET = new HashSet<>();
    private static final Set<String> WORKDAY_SET = new HashSet<>();

    static {
        HOLIDAY_SET.addAll(Arrays.asList(HOLIDAYS_2025));
        HOLIDAY_SET.addAll(Arrays.asList(HOLIDAYS_2026));
        WORKDAY_SET.addAll(Arrays.asList(WORKDAYS_2025));
        WORKDAY_SET.addAll(Arrays.asList(WORKDAYS_2026));
    }

    /**
     * 判断日期是否为法定假日
     */
    public static boolean isHoliday(LocalDate date) {
        return HOLIDAY_SET.contains(date.toString());
    }

    /**
     * 判断日期是否为调休上班日
     */
    public static boolean isWorkday(LocalDate date) {
        return WORKDAY_SET.contains(date.toString());
    }

    /**
     * 获取假日名称
     */
    public static String getHolidayName(LocalDate date) {
        return HOLIDAY_NAME_MAP.getOrDefault(date.toString(), "");
    }

    /**
     * 获取指定年份的假日数据（用于API返回）
     */
    public static Map<String, Object> getHolidayData(Integer year) {
        Map<String, String> holidays = new LinkedHashMap<>();
        Map<String, Boolean> workdays = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : HOLIDAY_NAME_MAP.entrySet()) {
            if (year == null || entry.getKey().startsWith(year + "-")) {
                holidays.put(entry.getKey(), entry.getValue());
            }
        }

        for (String workday : WORKDAY_SET) {
            if (year == null || workday.startsWith(year + "-")) {
                workdays.put(workday, true);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("holidays", holidays);
        result.put("workdays", workdays);
        return result;
    }
}
