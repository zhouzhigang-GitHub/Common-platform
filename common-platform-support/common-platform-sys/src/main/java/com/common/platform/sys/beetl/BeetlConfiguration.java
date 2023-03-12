package com.common.platform.sys.beetl;

import com.common.platform.auth.context.LoginContext;
import com.common.platform.base.consts.ConstantsContext;
import com.common.platform.base.utils.CoreUtil;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

/**
 * beetl拓展配置,绑定一些工具类,方便在模板中直接调用
 */
public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    private LoginContext loginContext;

    public BeetlConfiguration(LoginContext loginContext) {
        this.loginContext = loginContext;
    }

    @Override
    public void initOther() {
        groupTemplate.registerFunctionPackage("shiro", loginContext);
        groupTemplate.registerFunctionPackage("tool", new CoreUtil());
        groupTemplate.registerFunctionPackage("constants", new ConstantsContext());
    }
}
