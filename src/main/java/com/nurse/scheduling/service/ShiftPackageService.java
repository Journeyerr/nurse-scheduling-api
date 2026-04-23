package com.nurse.scheduling.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nurse.scheduling.dto.package_.ShiftPackageCreateRequest;
import com.nurse.scheduling.dto.package_.ShiftPackageResponse;
import com.nurse.scheduling.entity.ShiftPackage;
import com.nurse.scheduling.mapper.ShiftPackageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 班种套餐服务
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class ShiftPackageService {

    @Autowired
    private ShiftPackageMapper shiftPackageMapper;

    /**
     * 获取套餐列表
     */
    public List<ShiftPackageResponse> getPackageList(String departmentId) {
        LambdaQueryWrapper<ShiftPackage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShiftPackage::getDepartmentId, departmentId)
                .orderByDesc(ShiftPackage::getCreateTime);
        
        List<ShiftPackage> packages = shiftPackageMapper.selectList(queryWrapper);
        
        return packages.stream().map(pkg -> {
            ShiftPackageResponse response = new ShiftPackageResponse();
            response.setId(pkg.getId());
            response.setName(pkg.getName());
            response.setShifts(pkg.getShifts());
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 创建套餐
     */
    public ShiftPackageResponse createPackage(String departmentId, ShiftPackageCreateRequest request) {
        ShiftPackage pkg = new ShiftPackage();
        pkg.setDepartmentId(Long.parseLong(departmentId));
        pkg.setName(request.getName());
        pkg.setShifts(request.getShifts());
        
        shiftPackageMapper.insert(pkg);
        
        ShiftPackageResponse response = new ShiftPackageResponse();
        response.setId(pkg.getId());
        response.setName(pkg.getName());
        response.setShifts(pkg.getShifts());
        return response;
    }

    /**
     * 更新套班
     */
    public ShiftPackageResponse updatePackage(ShiftPackageCreateRequest request) {
        ShiftPackage pkg = shiftPackageMapper.selectById(request.getId());
        if (pkg == null) {
            throw new RuntimeException("套餐不存在");
        }
        
        pkg.setName(request.getName());
        pkg.setShifts(request.getShifts());
        
        shiftPackageMapper.updateById(pkg);
        
        ShiftPackageResponse response = new ShiftPackageResponse();
        response.setId(pkg.getId());
        response.setName(pkg.getName());
        response.setShifts(pkg.getShifts());
        return response;
    }

    /**
     * 删除套餐
     */
    public void deletePackage(String id) {
        shiftPackageMapper.deleteById(id);
    }
}
