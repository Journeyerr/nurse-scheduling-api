package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.Result;
import com.nurse.scheduling.dto.swap.SwapRequestDto;
import com.nurse.scheduling.service.ExpectScheduleService;
import com.nurse.scheduling.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 换班控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/swap")
public class SwapController {

    @Autowired
    private ExpectScheduleService expectScheduleService;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 提交换班申请
     */
    @PostMapping("/submit")
    public Result<Void> submitSwapRequest(@RequestBody SwapRequestDto request) {
        log.info("提交换班申请：{}", request);
        expectScheduleService.submitSwapRequest(request);
        return Result.ok();
    }

    /**
     * 获取指定日期排班的用户列表
     */
    @GetMapping("/users-by-date")
    public Result<List<Map<String, Object>>> getUsersByDate(@RequestParam String date) {
        log.info("获取日期 {} 的排班用户列表", date);
        List<Map<String, Object>> users = scheduleService.getUsersByDate(date);
        return Result.ok(users);
    }
}
