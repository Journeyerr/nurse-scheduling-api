package com.nurse.scheduling.service;

import java.util.Map;

/**
 * 微信登录服务接口
 *
 * @author nurse-scheduling
 */
public interface WxLoginService {

    /**
     * 通过code获取微信用户openid
     *
     * @param code 微信登录code
     * @return 包含openid和session_key的Map
     */
    Map<String, String> getOpenidByCode(String code);

    /**
     * 获取微信AccessToken
     *
     * @return access_token
     */
    String getAccessToken();
}
