package com.common.platform.sys.auth;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.common.platform.auth.context.LoginContextHolder;
import com.common.platform.auth.exception.AuthException;
import com.common.platform.auth.exception.enums.AuthExceptionEnum;
import com.common.platform.auth.jwt.JwtTokenUtil;
import com.common.platform.auth.jwt.payload.JwtPayLoad;
import com.common.platform.auth.pojo.LoginUser;
import com.common.platform.auth.service.AuthService;
import com.common.platform.base.config.context.HttpContext;
import com.common.platform.base.config.context.SpringContextHolder;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.auth.cache.SessionManager;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.factory.UserFactory;
import com.common.platform.sys.listener.ConfigListener;
import com.common.platform.sys.log.LogManager;
import com.common.platform.sys.log.factory.LogTaskFactory;
import com.common.platform.sys.modular.system.entity.User;
import com.common.platform.sys.modular.system.mapper.MenuMapper;
import com.common.platform.sys.modular.system.mapper.UserMapper;
import com.common.platform.sys.modular.system.service.DictService;
import com.common.platform.sys.state.ManagerStatus;
import com.common.platform.sys.util.SaltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.common.platform.base.config.context.HttpContext.getIp;
import static com.common.platform.base.consts.ConstantsContext.getJwtSecretExpireSec;
import static com.common.platform.base.consts.ConstantsContext.getTokenHeaderName;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class  AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private SessionManager sessionManager;

    public static AuthService me() {
        return SpringContextHolder.getBean(AuthService.class);
    }

    @Override
    public String login(String username, String password) {

        User user = userMapper.getByAccount(username);

        // 账号不存在
        if (null == user) {
            throw new AuthException(AuthExceptionEnum.USERNAME_PWD_ERROR);
        }

        //验证账号密码是否正确
        String requestMd5 = SaltUtil.md5Encrypt(password, user.getSalt());
        String dbMd5 = user.getPassword();
        if (dbMd5 == null || !dbMd5.equalsIgnoreCase(requestMd5)) {
            throw new AuthException(AuthExceptionEnum.USERNAME_PWD_ERROR);
        }

        return login(username);
    }

    @Override
    public String login(String username) {

        User user = userMapper.getByAccount(username);

        // 账号不存在
        if (null == user) {
            throw new AuthException(AuthExceptionEnum.USERNAME_PWD_ERROR);
        }

        // 账号被冻结
        if (!user.getStatus().equals(ManagerStatus.OK.getCode())) {
            throw new AuthException(AuthExceptionEnum.ACCOUNT_FREEZE_ERROR);
        }

        //记录登录日志
        LogManager.me().executeLog(LogTaskFactory.loginLog(user.getUserId(), getIp()));

        //TODO key的作用
        JwtPayLoad payLoad = new JwtPayLoad(user.getUserId(), user.getAccount(), "xxxx");

        //创建token
        String token = JwtTokenUtil.generateToken(payLoad);

        //创建登录会话
        sessionManager.createSession(token, user(username));

        //创建cookie
        addLoginCookie(token);

        return token;
    }

    @Override
    public void addLoginCookie(String token) {
        //创建cookie
        Cookie authorization = new Cookie(getTokenHeaderName(), token);
        authorization.setMaxAge(getJwtSecretExpireSec().intValue());
        authorization.setHttpOnly(true);
        authorization.setPath("/");
        HttpServletResponse response = HttpContext.getResponse();
        response.addCookie(authorization);
    }

    @Override
    public void logout() {
        String token = LoginContextHolder.getContext().getToken();
        logout(token);
    }

    @Override
    public void logout(String token) {

        //记录退出日志
        LogManager.me().executeLog(LogTaskFactory.exitLog(LoginContextHolder.getContext().getUser().getId(), getIp()));

        //删除Auth相关cookies
        Cookie[] cookies = HttpContext.getRequest().getCookies();
        for (Cookie cookie : cookies) {
            String tokenHeader = getTokenHeaderName();
            if (tokenHeader.equalsIgnoreCase(cookie.getName())) {
                Cookie temp = new Cookie(cookie.getName(), "");
                temp.setMaxAge(0);
                HttpContext.getResponse().addCookie(temp);
            }
        }

        //删除会话
        sessionManager.removeSession(token);
    }

    @Override
    public LoginUser user(String account) {

        User user = userMapper.getByAccount(account);

        LoginUser loginUser = UserFactory.createLoginUser(user);

        //用户角色数组
        Long[] roleArray = Convert.toLongArray(user.getRoleId());

        //如果角色是空就直接返回
        if (roleArray == null || roleArray.length == 0) {
            return loginUser;
        }

        //获取用户角色列表
        List<Long> roleList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();
        List<String> roleTipList = new ArrayList<>();
        for (Long roleId : roleArray) {
            roleList.add(roleId);
            roleNameList.add(ConstantFactory.me().getSingleRoleName(roleId));
            roleTipList.add(ConstantFactory.me().getSingleRoleTip(roleId));
        }
        loginUser.setRoleList(roleList);
        loginUser.setRoleNames(roleNameList);
        loginUser.setRoleTips(roleTipList);

        //根据角色获取系统的类型
        List<String> systemTypes = this.menuMapper.getMenusTypesByRoleIds(roleList);

        //通过字典编码
        List<Map<String, Object>> dictsByCodes = dictService.getDictsByCodes(systemTypes);
        loginUser.setSystemTypes(dictsByCodes);

        //设置权限列表
        Set<String> permissionSet = new HashSet<>();
        for (Long roleId : roleList) {
            List<String> permissions = this.findPermissionByRoleId(roleId);
            if (permissions != null) {
                for (String permission : permissions) {
                    if (CoreUtil.isNotEmpty(permission)) {
                        permissionSet.add(permission);
                    }
                }
            }
        }
        loginUser.setPermissions(permissionSet);

        return loginUser;
    }

    /**
     * 看到问题了没，方法名不对，对了一个s
     * @param roleId
     * @return
     */
    @Override
    public List<String> findPermissionByRoleId(Long roleId) {
        return menuMapper.getResUrlsByRoleId(roleId);
    }



    @Override
    public boolean check(String[] roleNames) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        if (null == user) {
            return false;
        }
        ArrayList<String> objects = CollectionUtil.newArrayList(roleNames);
        String join = CollectionUtil.join(objects, ",");
        if (LoginContextHolder.getContext().hasAnyRoles(join)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkAll() {
        HttpServletRequest request = HttpContext.getRequest();
        LoginUser user = LoginContextHolder.getContext().getUser();
        if (null == user) {
            return false;
        }
        String requestURI = request.getRequestURI().replaceFirst(ConfigListener.getConf().get("contextPath"), "");
        String[] str = requestURI.split("/");
        if (str.length > 3) {
            requestURI = "/" + str[1] + "/" + str[2];
        }
        if (LoginContextHolder.getContext().hasPermission(requestURI)) {
            return true;
        }
        return false;
    }



}
