package com.common.platform.sys.modular.system.wrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.base.page.PageResult;
import com.common.platform.sys.base.controller.warpper.BaseControllerWrapper;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.util.DecimalUtil;

import java.util.List;
import java.util.Map;

/**
 * 角色列表的包装类
 */
public class RoleWrapper extends BaseControllerWrapper {

    public RoleWrapper(Map<String, Object> single) {
        super(single);
    }

    public RoleWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public RoleWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public RoleWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("pName", ConstantFactory.me().getSingleRoleName(DecimalUtil.getLong(map.get("pid"))));
    }

}
