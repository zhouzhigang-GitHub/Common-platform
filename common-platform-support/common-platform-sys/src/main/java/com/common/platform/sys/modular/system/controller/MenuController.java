package com.common.platform.sys.modular.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.auth.annotation.Permission;
import com.common.platform.base.config.sys.Const;
import com.common.platform.base.exception.BizExceptionEnum;
import com.common.platform.base.exception.ServiceException;
import com.common.platform.base.log.BussinessLog;
import com.common.platform.base.page.LayuiPageInfo;
import com.common.platform.base.page.factory.LayuiPageFactory;
import com.common.platform.base.pojo.tree.node.ZTreeNode;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.base.controller.BaseController;
import com.common.platform.sys.base.controller.response.ResponseData;
import com.common.platform.sys.dictmap.DeleteDict;
import com.common.platform.sys.dictmap.MenuDict;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.log.LogObjectHolder;
import com.common.platform.sys.modular.system.entity.Menu;
import com.common.platform.sys.modular.system.model.MenuDto;
import com.common.platform.sys.modular.system.service.MenuService;
import com.common.platform.sys.modular.system.service.UserService;
import com.common.platform.sys.modular.system.wrapper.MenuWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private static String PREFIX = "/modular/system/menu/";

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    /**
     * 跳转到菜单列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "menu.html";
    }

    /**
     * 跳转到菜单添加页面
     */
    @RequestMapping(value = "/menu_add")
    public String menuAdd() {
        return PREFIX + "menu_add.html";
    }

    /**
     * 跳转到菜单编辑页面
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/menu_edit")
    public String menuEdit(@RequestParam Long menuId) {
        if (CoreUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //获取菜单当前信息，记录日志用
        Menu menu = this.menuService.getById(menuId);
        LogObjectHolder.me().set(menu);

        return PREFIX + "menu_edit.html";
    }

    /**
     * 新增菜单
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/add")
    @BussinessLog(value = "菜单新增", key = "name", dict = MenuDict.class)
    @ResponseBody
    public ResponseData add(MenuDto menu) {
        this.menuService.addMenu(menu);
        return SUCCESS_TIP;
    }

    /**
     * 删除菜单
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除菜单", key = "menuId", dict = DeleteDict.class)
    @ResponseBody
    public ResponseData remove(@RequestParam Long menuId) {
        if (CoreUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(menuId));

        this.menuService.delMenuContainSubMenus(menuId);

        return SUCCESS_TIP;
    }

    /**
     * 查看菜单
     */
    @RequestMapping(value = "/view/{menuId}")
    @ResponseBody
    public ResponseData view(@PathVariable Long menuId) {
        if (CoreUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Menu menu = this.menuService.getById(menuId);
        return ResponseData.success(menu);
    }

    /**
     * 修改菜单
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改菜单", key = "name", dict = MenuDict.class)
    @ResponseBody
    public ResponseData edit(MenuDto menu) {

        //如果修改了编号，则该菜单的子菜单也要修改对应编号
        this.menuService.updateMenu(menu);

        // TODO 刷新当前用户菜单
//        this.userService.refreshCurrentUser();

        return SUCCESS_TIP;
    }

    /**
     * 获取菜单列表
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String menuName,
                       @RequestParam(required = false) String level,
                       @RequestParam(required = false) Long menuId) {
        Page<Map<String, Object>> menus = this.menuService.selectMenus(menuName, level, menuId);
        Page<Map<String, Object>> wrap = new MenuWrapper(menus).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 获取菜单列表（s树形）
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/listTree")
    @ResponseBody
    public Object listTree(@RequestParam(required = false) String menuName,
                           @RequestParam(required = false) String level) {
        List<Map<String, Object>> menus = this.menuService.selectMenuTree(menuName, level);
        List<Map<String, Object>> menusWrap = new MenuWrapper(menus).wrap();

        LayuiPageInfo result = new LayuiPageInfo();
        result.setData(menusWrap);
        return result;
    }

    /**
     * 获取菜单信息
     */
    @RequestMapping(value = "/getMenuInfo")
    @ResponseBody
    public ResponseData getMenuInfo(@RequestParam Long menuId) {
        if (CoreUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        Menu menu = this.menuService.getById(menuId);

        MenuDto menuDto = new MenuDto();
        BeanUtil.copyProperties(menu, menuDto);

        //设置pid和父级名称
        menuDto.setPid(ConstantFactory.me().getMenuIdByCode(menuDto.getPcode()));
        menuDto.setPcodeName(ConstantFactory.me().getMenuNameByCode(menuDto.getPcode()));

        return ResponseData.success(menuDto);
    }

    /**
     * 获取菜单列表(首页用)
     */
    @RequestMapping(value = "/menuTreeList")
    @ResponseBody
    public List<ZTreeNode> menuTreeList() {
        return this.menuService.menuTreeList();
    }

    /**
     * 获取菜单列表(选择父级菜单用)
     */
    @RequestMapping(value = "/selectMenuTreeList")
    @ResponseBody
    public List<ZTreeNode> selectMenuTreeList() {
        List<ZTreeNode> roleTreeList = this.menuService.menuTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色的菜单列表
     */
    @RequestMapping(value = "/menuTreeListByRoleId/{roleId}")
    @ResponseBody
    public List<ZTreeNode> menuTreeListByRoleId(@PathVariable Long roleId) {
        List<Long> menuIds = this.menuService.getMenuIdsByRoleId(roleId);
        if (CoreUtil.isEmpty(menuIds)) {
            return this.menuService.menuTreeList();
        } else {
            return this.menuService.menuTreeListByMenuIds(menuIds);
        }
    }
}
