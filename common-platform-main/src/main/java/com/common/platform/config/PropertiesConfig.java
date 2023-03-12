package com.common.platform.config;

import com.common.platform.sys.beetl.BeetlProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 项目中的配置
 */
@Configuration
public class PropertiesConfig {

    /**
     * beetl模板的配置
     */
    @Bean
    @ConfigurationProperties(prefix = BeetlProperties.BEETLCONF_PREFIX)
    public BeetlProperties beetlProperties() {
        return new BeetlProperties();
    }

}
