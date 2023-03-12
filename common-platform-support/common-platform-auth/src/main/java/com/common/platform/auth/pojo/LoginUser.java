package com.common.platform.auth.pojo;

import com.common.platform.base.utils.CoreUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
/**
 * 在正式开发权限模块时添加implements UserDetails和相关处理方法
 */
public class LoginUser implements UserDetails,Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户主键ID
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 角色集
     */
    private List<Long> roleList;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 角色名称集
     */
    private List<String> roleNames;

    /**
     * 角色备注（code）
     */
    private List<String> roleTips;

    /**
     * 系统标识集合
     */
    private List<Map<String, Object>> systemTypes;

    /**
     * 拥有的权限
     */
    private Set<String> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (CoreUtil.isNotEmpty(this.roleNames)) {
            for (String roleName : this.roleNames) {
                GrantedAuthority grantedAuthority = (GrantedAuthority) () -> roleName;
                grantedAuthorities.add(grantedAuthority);
            }
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        //能生成loginUser就是jwt解析成功，没锁定
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //能生成loginUser就是jwt解析成功，没锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //能生成loginUser就是jwt解析成功，没锁定
        return true;
    }

    @Override
    public boolean isEnabled() {
        //能生成loginUser就是jwt解析成功，没锁定
        return true;
    }
}
