package com.common.platform.sys.modular.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.platform.base.config.sys.Const;
import com.common.platform.base.exception.BizExceptionEnum;
import com.common.platform.base.exception.ServiceException;
import com.common.platform.base.page.factory.LayuiPageFactory;
import com.common.platform.base.pojo.tree.node.ZTreeNode;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.exception.RequestEmptyException;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.log.LogObjectHolder;
import com.common.platform.sys.modular.system.entity.Relation;
import com.common.platform.sys.modular.system.entity.Role;
import com.common.platform.sys.modular.system.mapper.RelationMapper;
import com.common.platform.sys.modular.system.mapper.RoleMapper;
import com.common.platform.sys.modular.system.model.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 角色表 服务实现类
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    @Autowired
    private RelationMapper relationMapper;

    /**
     * 添加角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRole(Role role) {

        if (CoreUtil.isOneEmpty(role, role.getName(), role.getPid(), role.getDescription())) {
            throw new RequestEmptyException();
        }

        role.setRoleId(null);

        this.save(role);
    }

    /**
     * 编辑角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void editRole(RoleDto roleDto) {

        if (CoreUtil.isOneEmpty(roleDto, roleDto.getName(), roleDto.getPid(), roleDto.getDescription())) {
            throw new RequestEmptyException();
        }

        Role old = this.getById(roleDto.getRoleId());
        BeanUtil.copyProperties(roleDto, old);
        this.updateById(old);

    }

    /**
     * 设置某个角色的权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void setAuthority(Long roleId, String ids) {
        // 删除该角色所有的权限
        this.baseMapper.deleteRolesById(roleId);

        // 添加新的权限
        for (Long id : Convert.toLongArray(ids.split(","))) {
            Relation relation = new Relation();
            relation.setRoleId(roleId);
            relation.setMenuId(id);
            this.relationMapper.insert(relation);
        }

        // 刷新当前用户的权限
        // userService.refreshCurrentUser();
    }

    /**
     * 删除角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void delRoleById(Long roleId) {

        if (CoreUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //不能删除超级管理员角色
        if (roleId.equals(Const.ADMIN_ROLE_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }

        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));

        //删除角色
        this.baseMapper.deleteById(roleId);

        //删除该角色所有的权限
        this.baseMapper.deleteRolesById(roleId);

    }

    /**
     * 根据条件查询角色列表
     */
    public Page<Map<String, Object>> selectRoles(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectRoles(page, condition);
    }

    /**
     * 删除某个角色的所有权限
     */
    public int deleteRolesById(Long roleId) {
        return this.baseMapper.deleteRolesById(roleId);
    }

    /**
     * 获取角色列表树
     */
    public List<ZTreeNode> roleTreeList() {
        return this.baseMapper.roleTreeList();
    }

    /**
     * 获取角色列表树
     */
    public List<ZTreeNode> roleTreeListByRoleId(Long[] roleId) {
        return this.baseMapper.roleTreeListByRoleId(roleId);
    }

    /**
     * 获取角色列表
     */
    public IPage listRole(String name) {
        Page pageContext = LayuiPageFactory.defaultPage();
        return baseMapper.listRole(pageContext, name);
    }
}
