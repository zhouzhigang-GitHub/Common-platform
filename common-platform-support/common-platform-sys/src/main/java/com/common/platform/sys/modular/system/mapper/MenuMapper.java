package com.common.platform.sys.modular.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.base.pojo.tree.node.MenuNode;
import com.common.platform.base.pojo.tree.node.ZTreeNode;
import com.common.platform.sys.modular.system.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 菜单表 Mapper接口
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据条件查询菜单
     */
    Page<Map<String, Object>> selectMenus(@Param("page") Page page, @Param("condition") String condition, @Param("level") String level, @Param("menuId") Long menuId, @Param("code") String code);

    /**
     * 根据条件查询菜单
     */
    List<Long> getMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取菜单列表树
     */
    List<ZTreeNode> menuTreeList();

    /**
     * 获取菜单列表树
     */
    List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds);

    /**
     * 删除menu关联的relation
     */
    int deleteRelationByMenu(@Param("menuId") Long menuId);

    /**
     * 获取资源url通过角色id
     */
    List<String> getResUrlsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色获取菜单
     */
    List<MenuNode> getMenusByRoleIds(List<Long> roleIds);

    /**
     * 根据角色获取菜单的类型列表
     */
    List<String> getMenusTypesByRoleIds(List<Long> roleIds);

    /**
     * 查询菜单树形列表
     */
    List<Map<String, Object>> selectMenuTree(@Param("condition") String condition, @Param("level") String level);

    /**
     * 获取pcodes like某个code的菜单列表
     */
    List<Menu> getMenusLikePcodes(@Param("code") String code);

}
