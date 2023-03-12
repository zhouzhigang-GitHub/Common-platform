package com.common.platform.sys.auth.cache;

import com.common.platform.auth.pojo.LoginUser;

/**
 * 会话管理
 */
public interface SessionManager {

    /**
     * 缓存前缀
     */
    String SESSION_PREFIX = "LOGIN_USER_";

    /**
     * 创建会话
     */
    void createSession(String token, LoginUser loginUser);

    /**
     * 获取会话
     */
    LoginUser getSession(String token);

    /**
     * 删除会话
     */
    void removeSession(String token);

    /**
     * 是否已经登陆
     */
    boolean haveSession(String token);

}
