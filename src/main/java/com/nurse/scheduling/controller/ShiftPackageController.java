package com.nurse.scheduling.controller;

import com.nurse.scheduling.common.Result;
import com.nurse.scheduling.dto.package_.ShiftPackageCreateRequest;
import com.nurse.scheduling.dto.package_.ShiftPackageResponse;
import com.nurse.scheduling.service.ShiftPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 班种套餐控制器
 *
 * @author nurse-scheduling
 */
@Slf4j
@RestController
@RequestMapping("/package")
public class ShiftPackageController {

    @Autowired
    private ShiftPackageService shiftPackageService;

    /**
     * 获取套餐列表
     */
    @GetMapping("/list")
    public Result<List<ShiftPackageResponse>> getPackageList(@RequestParam String departmentId) {
        log.info("获取套餐列表，科室ID：{}", departmentId);
        List<ShiftPackageResponse> response = shiftPackageService.getPackageList(departmentId);
        return Result.ok(response);
    }

    /**
     * 创建套餐
     */
    @PostMapping("/create")
    public Result<ShiftPackageResponse> createPackage(@RequestParam String departmentId,
                                                       @Valid @RequestBody ShiftPackageCreateRequest request) {
        log.info("创建套餐，科室ID：{}", departmentId);
        ShiftPackageResponse response = shiftPackageService.createPackage(departmentId, request);
        return Result.ok(response);
    }

    /**
     * 更新套餐
     */
    @PutMapping("/update")
    public Result<ShiftPackageResponse> updatePackage(@Valid @RequestBody ShiftPackageCreateRequest request) {
        log.info("更新套餐，套餐ID：{}", request.getId());
        ShiftPackageResponse response = shiftPackageService.updatePackage(request);
        return Result.ok(response);
    }

    /**
     * 删除套餐
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deletePackage(@PathVariable String id) {
        log.info("删除套班，套班ID：{}", id);
        shiftPackageService.deletePackage(id);
        return Result.ok();
    }
}
