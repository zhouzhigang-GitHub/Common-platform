package com.common.platform.sys.modular.config.controller;

import com.common.platform.base.page.LayuiPageInfo;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.base.controller.BaseController;
import com.common.platform.sys.base.controller.response.ResponseData;
import com.common.platform.sys.modular.config.entity.Config;
import com.common.platform.sys.modular.config.model.params.ConfigParam;
import com.common.platform.sys.modular.config.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 参数配置控制器
 */
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {

    private String PREFIX = "/modular/config";

    @Autowired
    private ConfigService configService;

    /**
     * 跳转到主页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/config.html";
    }

    /**
     * 新增页面
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/config_add.html";
    }

    /**
     * 编辑页面
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/config_edit.html";
    }

    /**
     * 新增接口
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(ConfigParam sysConfigParam) {
        this.configService.add(sysConfigParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(ConfigParam sysConfigParam) {
        this.configService.update(sysConfigParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(ConfigParam sysConfigParam) {
        this.configService.delete(sysConfigParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(ConfigParam sysConfigParam) {
        Config detail = this.configService.getById(sysConfigParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(@RequestParam(value = "condition", required = false) String condition) {

        ConfigParam sysConfigParam = new ConfigParam();

        if (CoreUtil.isNotEmpty(condition)) {
            sysConfigParam.setCode(condition);
            sysConfigParam.setName(condition);
            sysConfigParam.setRemark(condition);
            sysConfigParam.setValue(condition);
        }
        System.out.println(this.configService.findPageBySpec(sysConfigParam));
        return this.configService.findPageBySpec(sysConfigParam);
    }

}
