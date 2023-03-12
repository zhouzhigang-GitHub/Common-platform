package com.common.platform.sys.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.sys.modular.system.entity.LoginLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 登录记录 Mapper 接口
 */
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    /**
     * 获取登录日志
     */
    List<Map<String, Object>> getLoginLogs(@Param("page") Page page, @Param("beginTime") String beginTime,
                                           @Param("endTime") String endTime, @Param("logName") String logName);
}
