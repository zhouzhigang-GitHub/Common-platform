package com.common.platform.sys.modular.system.controller;

import com.common.platform.auth.context.LoginContextHolder;
import com.common.platform.auth.service.AuthService;
import com.common.platform.base.consts.ConstantsContext;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.auth.cache.SessionManager;
import com.common.platform.sys.base.controller.BaseController;
import com.common.platform.sys.base.controller.response.ResponseData;
import com.common.platform.sys.base.controller.response.SuccessResponseData;
import com.common.platform.sys.exception.InvalidKaptchaException;
import com.common.platform.sys.exception.RequestEmptyException;
import com.common.platform.sys.modular.system.service.UserService;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录控制器
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        //判断用户是否登录
        if (LoginContextHolder.getContext().hasLogin()) {
            Map<String, Object> userIndexInfo = userService.getUserIndexInfo();

            //用户信息为空，提示账号没分配角色登录不进去
            if (userIndexInfo == null) {
                model.addAttribute("tips", "该用户没有角色，无法登陆");
                return "/login.html";
            } else {
                model.addAllAttributes(userIndexInfo);
                return "/index.html";
            }

        } else {
            return "/login.html";
        }
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (LoginContextHolder.getContext().hasLogin()) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData loginVali(HttpServletRequest request, HttpServletResponse response) {

        String username = super.getPara("username");
        String password = super.getPara("password");

        if (CoreUtil.isOneEmpty(username, password)) {
            throw new RequestEmptyException("账号或密码为空！");
        }

        //验证验证码是否正确
        if (ConstantsContext.getKaptchaOpen()) {
            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (CoreUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }

        //登录并创建token
        String token = authService.login(username, password);

        return new SuccessResponseData(token);
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public ResponseData logOut() {
        authService.logout();
        return new SuccessResponseData();
    }

}
