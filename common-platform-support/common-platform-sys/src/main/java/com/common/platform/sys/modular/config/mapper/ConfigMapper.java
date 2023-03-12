package com.common.platform.sys.modular.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.sys.modular.config.entity.Config;
import com.common.platform.sys.modular.config.model.params.ConfigParam;
import com.common.platform.sys.modular.config.model.result.ConfigResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 参数配置 Mapper 接口
 */
public interface ConfigMapper extends BaseMapper<Config> {

    /**
     * 获取列表
     */
    List<ConfigResult> customList(@Param("paramCondition") ConfigParam paramCondition);

    /**
     * 获取map列表
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ConfigParam paramCondition);

    /**
     * 获取分页实体列表
     */
    Page<ConfigResult> customPageList(@Param("page") Page page, @Param("paramCondition") ConfigParam paramCondition);

    /**
     * 获取分页map列表
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ConfigParam paramCondition);

}
