package com.common.platform.config;

import com.common.platform.auth.aop.PermissionAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 资源过滤的aop
 */
@Configuration
public class PermissionAopConfig {

    /**
     * 资源过滤的aop
     */
    @Bean
    public PermissionAop permissionAop() {
        return new PermissionAop();
    }

}
