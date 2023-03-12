package com.common.platform.sys.modular.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.platform.base.page.LayuiPageInfo;
import com.common.platform.sys.modular.config.entity.Config;
import com.common.platform.sys.modular.config.model.params.ConfigParam;
import com.common.platform.sys.modular.config.model.result.ConfigResult;

import java.util.List;


/**
 * 参数配置 服务类
 */
public interface ConfigService extends IService<Config> {

    /**
     * 新增
     */
    void add(ConfigParam param);

    /**
     * 删除
     */
    void delete(ConfigParam param);

    /**
     * 更新
     */
    void update(ConfigParam param);

    /**
     * 查询单条数据，Specification模式
     */
    ConfigResult findBySpec(ConfigParam param);

    /**
     * 查询列表，Specification模式
     */
    List<ConfigResult> findListBySpec(ConfigParam param);

    /**
     * 查询分页数据，Specification模式
     */
    LayuiPageInfo findPageBySpec(ConfigParam param);

}
