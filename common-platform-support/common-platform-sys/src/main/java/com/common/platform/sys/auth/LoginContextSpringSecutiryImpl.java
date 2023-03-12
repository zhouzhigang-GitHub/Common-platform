package com.common.platform.sys.auth;

import com.common.platform.auth.context.LoginContext;
import com.common.platform.auth.exception.AuthException;
import com.common.platform.auth.exception.enums.AuthExceptionEnum;
import com.common.platform.auth.pojo.LoginUser;
import com.common.platform.base.config.sys.Const;
import com.common.platform.sys.factory.ConstantFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户登录上下文
 */
@Component
public class LoginContextSpringSecutiryImpl implements LoginContext {

    @Override
    public LoginUser getUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
            throw new AuthException(AuthExceptionEnum.NOT_LOGIN_ERROR);
        } else {
            return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public boolean hasLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        } else {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public Long getUserId() {
        return getUser().getId();
    }

    @Override
    public boolean hasRole(String roleName) {
        return getUser().getRoleTips().contains(roleName);
    }

    @Override
    public boolean hasAnyRoles(String roleNames) {
        boolean hasAnyRole = false;
        if (this.hasLogin() && roleNames != null && roleNames.length() > 0) {
            for (String role : roleNames.split(",")) {
                if (hasRole(role.trim())) {
                    hasAnyRole = true;
                    break;
                }
            }
        }
        return hasAnyRole;
    }

    @Override
    public boolean hasPermission(String permission) {
        return getUser().getPermissions().contains(permission);
    }

    @Override
    public boolean isAdmin() {
        List<Long> roleList = getUser().getRoleList();
        for (Long integer : roleList) {
            String singleRoleTip = ConstantFactory.me().getSingleRoleTip(integer);
            if (singleRoleTip.equals(Const.ADMIN_NAME)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Long> getDeptDataScope() {
        Long deptId = getUser().getDeptId();
        List<Long> subDeptIds = ConstantFactory.me().getSubDeptId(deptId);
        subDeptIds.add(deptId);
        return subDeptIds;
    }
}
