package com.nurse.scheduling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurse.scheduling.dto.shift.ShiftCreateRequest;
import com.nurse.scheduling.dto.shift.ShiftResponse;
import com.nurse.scheduling.entity.Shift;

import java.util.List;

/**
 * 班种服务接口
 *
 * @author nurse-scheduling
 */
public interface ShiftService extends IService<Shift> {

    /**
     * 获取班种列表
     *
     * @param departmentId 科室ID
     * @return 班种列表
     */
    List<ShiftResponse> getShiftList(String departmentId);

    /**
     * 创建班种
     *
     * @param departmentId 科室ID
     * @param request 创建请求
     * @return 班种信息
     */
    ShiftResponse createShift(String departmentId, ShiftCreateRequest request);

    /**
     * 更新班种
     *
     * @param request 更新请求
     * @return 班种信息
     */
    ShiftResponse updateShift(ShiftCreateRequest request);

    /**
     * 删除班种
     *
     * @param shiftId 班种ID
     */
    void deleteShift(String shiftId);
}

