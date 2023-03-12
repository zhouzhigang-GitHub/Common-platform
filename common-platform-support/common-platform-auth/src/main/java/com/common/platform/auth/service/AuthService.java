package com.common.platform.auth.service;

import com.common.platform.auth.pojo.LoginUser;

import java.util.List;

public interface AuthService {
    /**
     * 登录
     * @param username 账号
     * @param password 密码
     * @return  token
     */
    String login(String username,String password);
    /**
     * 登录（直接用账号登录）
     * @param username
     * @return token
     */
    String login(String username);
    /**
     * 创建登录cookie
     * @param token
     */
    void addLoginCookie(String token);
    /**
     * 退出当前用户
     */
    void logout();
    /**
     * 退出
     *
     * @param token
     *
     */
    void logout(String token);
    /**
     * 根据账号获取登录用户
     * @param account 账号
     * @return loginUser
     */
    LoginUser user(String account);

    /**
     * 获取权限列表 通过角色Id
     *
     * @param roleId
     */
    List<String> findPermissionByRoleId(Long roleId);
    /**
     * 检查当前登录用户是否拥有指定角色
     */
    boolean check(String[] roleName);
    /**
     * 检查当前登录用户是否拥有当前请求的权限
     */
    boolean checkAll();
}
