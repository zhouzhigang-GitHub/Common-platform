package com.common.platform.sys.auth.userdetail;

import com.common.platform.auth.pojo.LoginUser;
import com.common.platform.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户详情信息获取
 */
@Service("jwtUserDetailsService")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthService authService;

    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return authService.user(username);
    }
}
