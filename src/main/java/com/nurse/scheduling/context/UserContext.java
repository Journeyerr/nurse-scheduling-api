package com.nurse.scheduling.context;

import com.nurse.scheduling.entity.User;
import com.nurse.scheduling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户上下文工具类
 * 提供获取当前登录用户信息的方法
 *
 * @author nurse-scheduling
 */
@Slf4j
@Component
public class UserContext {

    private static final ThreadLocal<String> USER_ID_HOLDER = new ThreadLocal<>();
    
    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        UserContext.userService = userService;
    }

    /**
     * 设置当前用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(String userId) {
        USER_ID_HOLDER.set(userId);
        log.debug("设置当前用户ID：{}", userId);
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID，未登录返回null
     */
    public static String getUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 获取当前用户ID（Long类型）
     *
     * @return 用户ID，未登录返回null
     */
    public static Long getUserIdAsLong() {
        String userId = getUserId();
        return userId != null ? Long.parseLong(userId) : null;
    }

    /**
     * 获取当前用户实体
     *
     * @return 用户实体，未登录或用户不存在返回null
     */
    public static User getUser() {
        Long userId = getUserIdAsLong();
        if (userId == null) {
            log.warn("获取用户实体失败：用户未登录");
            return null;
        }
        
        if (userService == null) {
            log.warn("获取用户实体失败：UserService未注入");
            return null;
        }
        
        User user = userService.getById(userId);
        if (user == null) {
            log.warn("获取用户实体失败：用户不存在，ID：{}", userId);
        }
        return user;
    }

    /**
     * 清除当前用户信息
     * 请求结束时调用，防止内存泄漏
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
        log.debug("清除当前用户信息");
    }
}
