package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.HolidayConstants;
import com.nurse.scheduling.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 假日控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/holiday")
public class HolidayController {

    /**
     * 获取假日数据
     * @param year 年份（可选，不传则返回所有年份）
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getHolidayList(@RequestParam(required = false) Integer year) {
        log.info("获取假日数据，年份：{}", year);
        Map<String, Object> data = HolidayConstants.getHolidayData(year);
        return Result.ok(data);
    }
}
