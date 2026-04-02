package com.nurse.scheduling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurse.scheduling.entity.DepartmentMember;
import com.nurse.scheduling.mapper.DepartmentMemberMapper;
import com.nurse.scheduling.service.DepartmentMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 科室成员服务实现类
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class DepartmentMemberServiceImpl extends ServiceImpl<DepartmentMemberMapper, DepartmentMember> implements DepartmentMemberService {
}
