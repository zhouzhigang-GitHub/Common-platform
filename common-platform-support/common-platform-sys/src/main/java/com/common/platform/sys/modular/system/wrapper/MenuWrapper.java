package com.common.platform.sys.modular.system.wrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.base.enums.YesOrNotEnum;
import com.common.platform.sys.base.controller.warpper.BaseControllerWrapper;
import com.common.platform.sys.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 菜单列表的包装类
 */
public class MenuWrapper extends BaseControllerWrapper {

    public MenuWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public MenuWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("statusName", ConstantFactory.me().getMenuStatusName((String) map.get("status")));

        String menuFlag = (String) map.get("menuFlag");
        for (YesOrNotEnum value : YesOrNotEnum.values()) {
            if (value.name().equals(menuFlag)) {
                map.put("isMenuName", value.getDesc());
            }
        }
    }

}
