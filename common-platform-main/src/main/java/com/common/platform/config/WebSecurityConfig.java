package com.common.platform.config;

import com.common.platform.sys.auth.entrypoint.JwtAuthenticationEntryPoint;
import com.common.platform.sys.auth.filter.JwtAuthorizationTokenFilter;
import com.common.platform.sys.auth.userdetail.JwtUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    private JwtAuthorizationTokenFilter authenticationTokenFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()

                //开启跨域
                .cors().and()

                //自定义退出
                .logout().disable()

                //禁用匿名用户
                //.anonymous().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // 全局不创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                .antMatchers("/").permitAll()

                //获取验证码
                .antMatchers("/kaptcha").permitAll()

                // 登录接口放开过滤
                .antMatchers("/login").permitAll()

                // session登录失效之后的跳转
                .antMatchers("/global/sessionError").permitAll()

                // 图片预览 头像
                .antMatchers("/system/preview/*").permitAll()

                // 错误页面的接口
                .antMatchers("/error").permitAll()
                .antMatchers("/global/error").permitAll()

                .anyRequest().authenticated();

        httpSecurity
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()
                .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        HttpMethod.POST,
                        "/login"
                )

                // 静态资源放开过滤
                .and()
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/assets/**",
                        "/favicon.ico"
                );

    }
}
