package com.common.platform.sys.auth.entrypoint;

import com.alibaba.fastjson.JSON;
import com.common.platform.auth.exception.enums.AuthExceptionEnum;
import com.common.platform.sys.base.controller.response.ErrorResponseData;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个端点在用户访问受保护资源但是不提供任何 token 的情况下
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final long serialVersionUID = -1L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // GET请求跳转到主页
        if ("get".equalsIgnoreCase(request.getMethod())
                && !request.getHeader("Accept").contains("application/json")) {

            response.sendRedirect(request.getContextPath() + "/global/sessionError");

        } else {

            // POST请求返回json
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");

            ErrorResponseData errorResponseData = new ErrorResponseData(
                    AuthExceptionEnum.NO_PERMISSION.getCode(), AuthExceptionEnum.NO_PERMISSION.getMessage());

            response.getWriter().write(JSON.toJSONString(errorResponseData));
        }
    }

}
