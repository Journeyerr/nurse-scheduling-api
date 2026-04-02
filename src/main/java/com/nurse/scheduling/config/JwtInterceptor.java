package com.nurse.scheduling.config;

import com.nurse.scheduling.context.UserContext;
import com.nurse.scheduling.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 *
 * @author nurse-scheduling
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 从请求头获取token
        String token = request.getHeader("Authorization");
        
        if (token == null || token.isEmpty()) {
            log.warn("请求未携带token，URI：{}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"success\":false,\"message\":\"未授权\"}");
            return false;
        }

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            log.warn("token验证失败，URI：{}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"success\":false,\"message\":\"token无效\"}");
            return false;
        }

        // 将用户ID放入上下文
        String userId = jwtUtil.getUserIdFromToken(token);
        UserContext.setUserId(userId);
        log.debug("token验证成功，用户ID：{}，URI：{}", userId, request.getRequestURI());
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除用户上下文，防止内存泄漏
        UserContext.clear();
    }
}
