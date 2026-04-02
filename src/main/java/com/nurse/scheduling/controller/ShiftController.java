package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.Result;
import com.nurse.scheduling.dto.shift.ShiftCreateRequest;
import com.nurse.scheduling.dto.shift.ShiftResponse;
import com.nurse.scheduling.service.ShiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 班种控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    /**
     * 获取班种列表
     */
    @GetMapping("/list")
    public Result<List<ShiftResponse>> getShiftList(@RequestParam String departmentId) {
        log.info("获取班种列表，科室ID：{}", departmentId);
        List<ShiftResponse> response = shiftService.getShiftList(departmentId);
        return Result.ok(response);
    }

    /**
     * 创建班种
     */
    @PostMapping("/create")
    public Result<ShiftResponse> createShift(@RequestParam String departmentId,
                                              @Valid @RequestBody ShiftCreateRequest createRequest) {
        log.info("创建班种，科室ID：{}", departmentId);
        ShiftResponse response = shiftService.createShift(departmentId, createRequest);
        return Result.ok(response);
    }

    /**
     * 更新班种
     */
    @PutMapping("/update")
    public Result<ShiftResponse> updateShift(@Valid @RequestBody ShiftCreateRequest updateRequest) {
        log.info("更新班种，班种ID：{}", updateRequest.getId());
        ShiftResponse response = shiftService.updateShift(updateRequest);
        return Result.ok(response);
    }

    /**
     * 删除班种
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteShift(@PathVariable String id) {
        log.info("删除班种，班种ID：{}", id);
        shiftService.deleteShift(id);
        return Result.ok();
    }
}
