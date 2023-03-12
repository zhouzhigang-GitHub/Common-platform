package com.common.platform.sys.modular.system.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.platform.auth.context.LoginContextHolder;
import com.common.platform.auth.pojo.LoginUser;
import com.common.platform.base.config.database.DataScope;
import com.common.platform.base.config.sys.Const;
import com.common.platform.base.exception.BizExceptionEnum;
import com.common.platform.base.exception.ServiceException;
import com.common.platform.base.page.factory.LayuiPageFactory;
import com.common.platform.base.pojo.tree.node.MenuNode;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.factory.UserFactory;
import com.common.platform.sys.modular.system.entity.User;
import com.common.platform.sys.modular.system.entity.UserPos;
import com.common.platform.sys.modular.system.mapper.UserMapper;
import com.common.platform.sys.modular.system.model.UserDto;
import com.common.platform.sys.state.ManagerStatus;
import com.common.platform.sys.util.DefaultImages;
import com.common.platform.sys.util.SaltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员表 服务实现类
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserPosService userPosService;

    @Autowired
    private MenuService menuService;

    /**
     * 添加用戶
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDto userDto) {

        // 判断账号是否重复
        User theUser = this.getByAccount(userDto.getAccount());
        if (theUser != null) {
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }

        // 完善账号信息
        String salt = SaltUtil.getRandomSalt();
        String password = SaltUtil.md5Encrypt(userDto.getPassword(), salt);

        User newUser = UserFactory.createUser(userDto, password, salt);
        this.save(newUser);

        //添加职位关联
        addPosition(userDto.getPosition(), newUser.getUserId());
    }

    /**
     * 修改用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void editUser(UserDto user) {
        User oldUser = this.getById(user.getUserId());

        if (LoginContextHolder.getContext().hasRole(Const.ADMIN_NAME)) {
            this.updateById(UserFactory.editUser(user, oldUser));
        } else {
            this.assertAuth(user.getUserId());
            LoginUser shiroUser = LoginContextHolder.getContext().getUser();
            if (shiroUser.getId().equals(user.getUserId())) {
                this.updateById(UserFactory.editUser(user, oldUser));
            } else {
                throw new ServiceException(BizExceptionEnum.NO_PERMITION);
            }
        }

        //删除职位关联
        userPosService.remove(new QueryWrapper<UserPos>().eq("user_id", user.getUserId()));

        //添加职位关联
        addPosition(user.getPosition(), user.getUserId());
    }

    /**
     * 删除用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {

        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        this.assertAuth(userId);
        this.setStatus(userId, ManagerStatus.DELETED.getCode());

        //删除职位关联
        userPosService.remove(new QueryWrapper<UserPos>().eq("user_id", userId));
    }

    /**
     * 修改用户状态
     */
    public int setStatus(Long userId, String status) {
        return this.baseMapper.setStatus(userId, status);
    }

    /**
     * 修改密码
     */
    public void changePwd(String oldPassword, String newPassword) {
        Long userId = LoginContextHolder.getContext().getUser().getId();
        User user = this.getById(userId);

        String oldMd5 = SaltUtil.md5Encrypt(oldPassword, user.getSalt());

        if (user.getPassword().equals(oldMd5)) {
            String newMd5 = SaltUtil.md5Encrypt(newPassword, user.getSalt());
            user.setPassword(newMd5);
            this.updateById(user);
        } else {
            throw new ServiceException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }

    /**
     * 根据条件查询用户列表
     */
    public Page<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectUsers(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 设置用户的角色
     */
    public int setRoles(Long userId, String roleIds) {
        return this.baseMapper.setRoles(userId, roleIds);
    }

    /**
     * 通过账号获取用户
     */
    public User getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限
     */
    public void assertAuth(Long userId) {
        if (LoginContextHolder.getContext().isAdmin()) {
            return;
        }
        List<Long> deptDataScope = LoginContextHolder.getContext().getDeptDataScope();
        User user = this.getById(userId);
        Long deptId = user.getDeptId();
        if (deptDataScope.contains(deptId)) {
            return;
        } else {
            throw new ServiceException(BizExceptionEnum.NO_PERMITION);
        }
    }

    /**
     * 获取用户的基本信息
     */
    public Map<String, Object> getUserInfo(Long userId) {
        User user = this.getById(userId);
        Map<String, Object> map = UserFactory.removeUnSafeFields(user);

        HashMap<String, Object> hashMap = CollectionUtil.newHashMap();
        hashMap.putAll(map);
        hashMap.put("roleName", ConstantFactory.me().getRoleName(user.getRoleId()));
        hashMap.put("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));

        return hashMap;
    }

    /**
     * 获取用户首页信息
     */
    public Map<String,Object> getUserIndexInfo(){
        LoginUser user = LoginContextHolder.getContext().getUser();
        List<Long> roleList = user.getRoleList();
        if(roleList==null || roleList.size()<=0){
            return null;
        }
        List<Map<String,Object>> menus = this.getUserMenuNodes(roleList);
        HashMap<String,Object> result = new HashMap<>();
        result.put("menus",menus);
        result.put("avatar", DefaultImages.defaultAvatarUrl());
        result.put("name",user.getName());
        return result;
    }

    /**
     * 获取用户菜单列表
     */
    public List<Map<String, Object>> getUserMenuNodes(List<Long> roleList) {
        if (roleList == null || roleList.size() == 0) {
            return new ArrayList<>();
        } else {
            List<MenuNode> menus = menuService.getMenusByRoleIds(roleList);

            //定义不同系统分类的菜单集合
            ArrayList<Map<String, Object>> lists = new ArrayList<>();

            //根据当前用户包含的系统类型，分类出不同的菜单
            List<Map<String, Object>> systemTypes = LoginContextHolder.getContext().getUser().getSystemTypes();
            for (Map<String, Object> systemType : systemTypes) {

                //当前遍历系统分类code
                String systemCode = (String) systemType.get("code");

                //获取当前系统分类下菜单集合
                ArrayList<MenuNode> originSystemTypeMenus = new ArrayList<>();
                for (MenuNode menu : menus) {
                    if (menu.getSystemType().equals(systemCode)) {
                        originSystemTypeMenus.add(menu);
                    }
                }

                //拼接存放key为系统分类编码，value为该分类下菜单集合的map
                HashMap<String, Object> map = new HashMap<>();
                List<MenuNode> treeSystemTypeMenus = MenuNode.buildTitle(originSystemTypeMenus);
                map.put("systemType", systemCode);
                map.put("menus", treeSystemTypeMenus);
                lists.add(map);
            }

            return lists;
        }
    }

    /**
     * 添加职位关联
     */
    private void addPosition(String positions, Long userId) {
        if (CoreUtil.isNotEmpty(positions)) {
            String[] position = positions.split(",");
            for (String item : position) {

                UserPos entity = new UserPos();
                entity.setUserId(userId);
                entity.setPosId(Long.valueOf(item));

                userPosService.save(entity);
            }
        }
    }

    /**
     * 选择办理人
     */
    public IPage listUserAndRoleExpectAdmin(Page pageContext) {
        return baseMapper.listUserAndRoleExpectAdmin(pageContext);
    }
}
