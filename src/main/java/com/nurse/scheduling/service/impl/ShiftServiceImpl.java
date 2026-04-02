package com.nurse.scheduling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurse.scheduling.dto.shift.ShiftCreateRequest;
import com.nurse.scheduling.dto.shift.ShiftResponse;
import com.nurse.scheduling.entity.Shift;
import com.nurse.scheduling.mapper.ShiftMapper;
import com.nurse.scheduling.service.ShiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 班种服务实现类
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class ShiftServiceImpl extends ServiceImpl<ShiftMapper, Shift> implements ShiftService {

    @Override
    public List<ShiftResponse> getShiftList(String departmentId) {
        log.info("获取班种列表，科室ID：{}", departmentId);
        
        List<Shift> shifts = lambdaQuery()
                .eq(Shift::getDepartmentId, Long.parseLong(departmentId))
                .orderByAsc(Shift::getCreateTime)
                .list();
        
        List<ShiftResponse> responseList = shifts.stream().map(shift -> {
            ShiftResponse response = new ShiftResponse();
            BeanUtil.copyProperties(shift, response);
            response.setId(shift.getId().toString());
            response.setTimeSlots(shift.getTimeSlots());
            return response;
        }).collect(Collectors.toList());
        
        log.debug("班种列表查询成功，班种数量：{}", responseList.size());
        return responseList;
    }

    @Override
    public ShiftResponse createShift(String departmentId, ShiftCreateRequest request) {
        log.info("创建班种，科室ID：{}，班种编号：{}", departmentId, request.getCode());
        
        Shift shift = new Shift();
        shift.setDepartmentId(Long.parseLong(departmentId));
        shift.setCode(request.getCode());
        shift.setName(request.getName());
        shift.setTimeSlots(request.getTimeSlots());
        shift.setDuration(request.getDuration());
        shift.setColor(request.getColor());
        
        save(shift);
        log.info("班种创建成功，班种ID：{}", shift.getId());
        
        ShiftResponse response = new ShiftResponse();
        BeanUtil.copyProperties(shift, response);
        response.setId(shift.getId().toString());
        response.setTimeSlots(request.getTimeSlots());
        
        return response;
    }

    @Override
    public ShiftResponse updateShift(ShiftCreateRequest request) {
        log.info("更新班种，班种ID：{}", request.getId());
        
        Shift shift = getById(Long.parseLong(request.getId()));
        if (shift == null) {
            log.warn("班种不存在，班种ID：{}", request.getId());
            return null;
        }
        
        shift.setCode(request.getCode());
        shift.setName(request.getName());
        shift.setTimeSlots(request.getTimeSlots());
        shift.setDuration(request.getDuration());
        shift.setColor(request.getColor());
        
        updateById(shift);
        log.info("班种更新成功，班种ID：{}", shift.getId());
        
        ShiftResponse response = new ShiftResponse();
        BeanUtil.copyProperties(shift, response);
        response.setId(shift.getId().toString());
        response.setTimeSlots(request.getTimeSlots());
        
        return response;
    }

    @Override
    public void deleteShift(String shiftId) {
        log.info("删除班种，班种ID：{}", shiftId);
        removeById(Long.parseLong(shiftId));
        log.info("班种删除成功，班种ID：{}", shiftId);
    }
}
