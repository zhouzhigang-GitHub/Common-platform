package com.common.platform.sys.modular.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.platform.sys.modular.system.entity.LoginLog;
import com.common.platform.sys.modular.system.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 登录记录 服务实现类
 */
@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLog> {

    /**
     * 获取登录日志列表
     */
    public List<Map<String, Object>> getLoginLogs(Page page, String beginTime, String endTime, String logName) {
        return this.baseMapper.getLoginLogs(page, beginTime, endTime, logName);
    }
}
