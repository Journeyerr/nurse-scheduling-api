package com.nurse.scheduling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 护士排班系统启动类
 *
 * @author nurse-scheduling
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.nurse.scheduling.mapper")
public class NurseSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(NurseSchedulingApplication.class, args);
        System.out.println("========================================");
        System.out.println("护士排班系统启动成功！");
        System.out.println("========================================");
    }
}
