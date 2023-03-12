package com.common.platform.sys.modular.system.wrapper;

import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.base.controller.warpper.BaseControllerWrapper;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.util.Contrast;
import com.common.platform.sys.util.DecimalUtil;

import java.util.List;
import java.util.Map;

/**
 * 日志列表的包装类
 */
public class LogWrapper extends BaseControllerWrapper {

    public LogWrapper(Map<String, Object> single) {
        super(single);
    }

    public LogWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String message = (String) map.get("message");

        Long userid = DecimalUtil.getLong(map.get("userId"));
        map.put("userName", ConstantFactory.me().getUserNameById(userid));

        //如果信息过长,则只截取前100位字符串
        if (CoreUtil.isNotEmpty(message) && message.length() >= 100) {
            String subMessage = message.substring(0, 100) + "...";
            map.put("message", subMessage);
        }

        //如果信息中包含分割符号;;;   则分割字符串返给前台
        if (CoreUtil.isNotEmpty(message) && message.contains(Contrast.separator)) {
            String[] msgs = message.split(Contrast.separator);
            map.put("regularMessage", msgs);
        } else {
            map.put("regularMessage", message);
        }
    }
}
