package com.nurse.scheduling.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nurse.scheduling.config.WxMiniappConfig;
import com.nurse.scheduling.service.WxLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信登录服务实现类
 *
 * @author nurse-scheduling
 */
@Slf4j
@Service
public class WxLoginServiceImpl implements WxLoginService {

    private static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    @Autowired
    private WxMiniappConfig wxMiniappConfig;

    @Override
    public Map<String, String> getOpenidByCode(String code) {
        log.info("微信登录，code：{}", code);

        String url = String.format(JSCODE2SESSION_URL,
                wxMiniappConfig.getAppid(),
                wxMiniappConfig.getSecret(),
                code);

        try {
            String result = HttpUtil.get(url);
            log.debug("微信登录响应：{}", result);

            JSONObject jsonObject = JSONUtil.parseObj(result);

            // 检查错误
            if (jsonObject.containsKey("errcode")) {
                Integer errcode = jsonObject.getInt("errcode");
                String errmsg = jsonObject.getStr("errmsg");
                log.error("微信登录失败，errcode：{}，errmsg：{}", errcode, errmsg);
                throw new RuntimeException("微信登录失败：" + errmsg);
            }

            Map<String, String> map = new HashMap<>();
            map.put("openid", jsonObject.getStr("openid"));
            map.put("sessionKey", jsonObject.getStr("session_key"));

            log.info("微信登录成功，openid：{}", map.get("openid"));
            return map;
        } catch (Exception e) {
            log.error("调用微信登录接口异常", e);
            throw new RuntimeException("微信登录失败：" + e.getMessage());
        }
    }

    @Override
    public String getAccessToken() {
        String url = String.format(ACCESS_TOKEN_URL,
                wxMiniappConfig.getAppid(),
                wxMiniappConfig.getSecret());

        try {
            String result = HttpUtil.get(url);
            log.debug("获取AccessToken响应：{}", result);

            JSONObject jsonObject = JSONUtil.parseObj(result);

            if (jsonObject.containsKey("errcode")) {
                Integer errcode = jsonObject.getInt("errcode");
                String errmsg = jsonObject.getStr("errmsg");
                log.error("获取AccessToken失败，errcode：{}，errmsg：{}", errcode, errmsg);
                throw new RuntimeException("获取AccessToken失败：" + errmsg);
            }

            return jsonObject.getStr("access_token");
        } catch (Exception e) {
            log.error("调用微信AccessToken接口异常", e);
            throw new RuntimeException("获取AccessToken失败：" + e.getMessage());
        }
    }
}
