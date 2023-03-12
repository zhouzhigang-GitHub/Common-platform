package com.common.platform.sys.modular.config.init;

import com.common.platform.base.consts.ConstantsContext;
import com.common.platform.sys.modular.config.entity.Config;
import com.common.platform.sys.modular.config.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 参数配置 服务类
 */
@Component
@Slf4j
public class ConfigInit implements CommandLineRunner {

    @Autowired
    private ConfigService configService;

    @Override
    public void run(String... args) {

        //初始化所有的常量
        List<Config> list = configService.list();

        if (list != null && list.size() > 0) {
            for (Config config : list) {
                ConstantsContext.putConstant(config.getCode(), config.getValue());
            }

            log.info("初始化常量" + list.size() + "条！");
        }

    }
}
