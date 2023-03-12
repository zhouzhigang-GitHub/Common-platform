package com.common.platform.sys.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.platform.base.config.context.SpringContextHolder;
import com.common.platform.base.enums.CommonStatus;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.cache.Cache;
import com.common.platform.sys.cache.CacheKey;
import com.common.platform.sys.log.LogObjectHolder;
import com.common.platform.sys.modular.system.entity.*;
import com.common.platform.sys.modular.system.mapper.*;
import com.common.platform.sys.modular.system.service.PositionService;
import com.common.platform.sys.modular.system.service.UserPosService;
import com.common.platform.sys.state.ManagerStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 快捷查询方法
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {

    private DeptMapper deptMapper = SpringContextHolder.getBean(DeptMapper.class);
    private DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);
    private DictTypeMapper dictTypeMapper = SpringContextHolder.getBean(DictTypeMapper.class);
    private PositionService positionService = SpringContextHolder.getBean(PositionService.class);
    private UserPosService userPosService = SpringContextHolder.getBean(UserPosService.class);
    private UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);


    public static IConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }

    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.DEPT_NAME + "'+#deptId")
    public String getDeptName(Long deptId) {
        if (deptId == null) {
            return "";
        } else if (deptId == 0L) {
            return "顶级";
        } else {
            Dept dept = deptMapper.selectById(deptId);
            if (CoreUtil.isNotEmpty(dept) && CoreUtil.isNotEmpty(dept.getFullName())) {
                return dept.getFullName();
            }
            return "";
        }
    }

    @Override
    public String getDictName(Long dictId) {
        if (CoreUtil.isEmpty(dictId)) {
            return "";
        } else {
            Dict dict = dictMapper.selectById(dictId);
            if (dict == null) {
                return "";
            } else {
                return dict.getName();
            }
        }
    }

    @Override
    public String getDictsByName(String name, String code) {
        DictType temp = new DictType();
        temp.setName(name);
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>(temp);
        DictType dictType = dictTypeMapper.selectOne(queryWrapper);
        if (dictType == null) {
            return "";
        } else {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper = wrapper.eq("dict_type_id", dictType.getDictTypeId());
            List<Dict> dicts = dictMapper.selectList(wrapper);
            for (Dict item : dicts) {
                if (item.getCode() != null && item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    @Override
    public String getDictNameByCode(String dictCode) {
        if (CoreUtil.isEmpty(dictCode)) {
            return "";
        } else {
            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("code", dictCode);
            Dict dict = dictMapper.selectOne(dictQueryWrapper);
            if (dict == null) {
                return "";
            } else {
                return dict.getName();
            }
        }
    }

    @Override
    public List<Dict> findInDict(Long id) {
        if (CoreUtil.isEmpty(id)) {
            return null;
        } else {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            List<Dict> dicts = dictMapper.selectList(wrapper.eq("pid", id));
            if (dicts == null || dicts.size() == 0) {
                return null;
            } else {
                return dicts;
            }
        }
    }

    @Override
    public List<Long> getSubDeptId(Long deptId) {
        ArrayList<Long> deptIds = new ArrayList<>();

        if (deptId == null) {
            return deptIds;
        } else {
            List<Dept> depts = this.deptMapper.likePids(deptId);
            if (depts != null && depts.size() > 0) {
                for (Dept dept : depts) {
                    deptIds.add(dept.getDeptId());
                }
            }

            return deptIds;
        }
    }

    @Override
    public List<Long> getParentDeptIds(Long deptId) {
        Dept dept = deptMapper.selectById(deptId);
        String pids = dept.getPids();
        String[] split = pids.split(",");
        ArrayList<Long> parentDeptIds = new ArrayList<>();
        for (String s : split) {
            parentDeptIds.add(Long.valueOf(StrUtil.removeSuffix(StrUtil.removePrefix(s, "["), "]")));
        }
        return parentDeptIds;
    }

    @Override
    public String getCacheObject(String para) {
        return LogObjectHolder.me().get().toString();
    }

    @Override
    public String getPositionName(Long userId) {
        StringBuilder positionNames = new StringBuilder();
        List<UserPos> userPosList = this.userPosService.list(new QueryWrapper<UserPos>());
        if (userPosList!=null && userPosList.size() > 0) {
            for (UserPos userPos : userPosList) {
                Position position = this.positionService.getById(userPos.getPosId());
                if (position != null) {
                    positionNames.append(",").append(position.getName());
                }
            }
        }
        return StrUtil.removePrefix(positionNames.toString(),",");
    }

    @Override
    public String getPositionIds(Long userId) {
        StringBuilder positionIds = new StringBuilder();
        List<UserPos> userPosList = this.userPosService.list(new QueryWrapper<UserPos>());
        if (userPosList != null && userPosList.size()>0) {
            for(UserPos userPos : userPosList) {
                Position position = this.positionService.getById(userPos.getPosId());
                if (position!=null) {
                    positionIds.append(",").append(position.getPositionId());
                }
            }
        }
        return StrUtil.removePrefix(positionIds.toString(),",");
    }


    @Override
    public String getUserNameById(Long userId) {
        User user = this.userMapper.selectById(userId);
        if(user==null){
            return "--";
        }else{
            return user.getName();
        }
    }

    @Override
    public String getUserAccountById(Long userId) {
        User user = this.userMapper.selectById(userId);
        if(user==null){
            return "--";
        }else{
            return user.getAccount();
        }
    }

    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.ROLES_NAME + "'+#roleIds")
    public String getRoleName(String roleIds) {
        if (CoreUtil.isEmpty(roleIds)) {
            return "";
        }
        Long[] roles = Convert.toLongArray(roleIds);
        StringBuilder sb = new StringBuilder();
        for (Long role : roles) {
            Role roleObj = roleMapper.selectById(role);
            if (CoreUtil.isNotEmpty(roleObj) && CoreUtil.isNotEmpty(roleObj.getName())) {
                sb.append(roleObj.getName()).append(",");
            }
        }
        return StrUtil.removeSuffix(sb.toString(), ",");
    }

    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
    public String getSingleRoleName(Long roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleMapper.selectById(roleId);
        if (CoreUtil.isNotEmpty(roleObj) && CoreUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getName();
        }
        return "";
    }

    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_TIP + "'+#roleId")
    public String getSingleRoleTip(Long roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleMapper.selectById(roleId);
        if (CoreUtil.isNotEmpty(roleObj) && CoreUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getDescription();
        }
        return "";
    }


    @Override
    public String getSexName(String sexCode) {
        return getDictsByName("性别",sexCode);
    }

    @Override
    public String getStatusName(String status) {
        return ManagerStatus.getDescription(status);
    }

    @Override
    public String getMenuNames(String menuIds) {
        Long[] menus = Convert.toLongArray(menuIds);
        StringBuilder sb = new StringBuilder();
        for (Long menu : menus) {
            Menu menuObj = menuMapper.selectById(menu);
            if (CoreUtil.isNotEmpty(menuObj) && CoreUtil.isNotEmpty(menuObj.getName())) {
                sb.append(menuObj.getName()).append(",");
            }
        }
        return StrUtil.removeSuffix(sb.toString(), ",");
    }

    @Override
    public String getMenuName(Long menuId) {
        if (CoreUtil.isEmpty(menuId)) {
            return "";
        } else {
            Menu menu = menuMapper.selectById(menuId);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    @Override
    public Menu getMenuByCode(String code) {
        if (CoreUtil.isEmpty(code)) {
            return new Menu();
        } else if (code.equals("0")) {
            return new Menu();
        } else {
            Menu param = new Menu();
            param.setCode(code);
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>(param);
            Menu menu = menuMapper.selectOne(queryWrapper);
            if (menu == null) {
                return new Menu();
            } else {
                return menu;
            }
        }
    }

    @Override
    public String getMenuNameByCode(String code) {
        if (CoreUtil.isEmpty(code)) {
            return "";
        } else if (code.equals("0")) {
            return "顶级";
        } else {
            Menu param = new Menu();
            param.setCode(code);
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>(param);
            Menu menu = menuMapper.selectOne(queryWrapper);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    @Override
    public Long getMenuIdByCode(String code) {
        if (CoreUtil.isEmpty(code)) {
            return 0L;
        } else if (code.equals("0")) {
            return 0L;
        } else {
            Menu menu = new Menu();
            menu.setCode(code);
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>(menu);
            Menu tempMenu = this.menuMapper.selectOne(queryWrapper);
            return tempMenu.getMenuId();
        }
    }

    @Override
    public String getMenuStatusName(String status) {
        return CommonStatus.getDescription(status);
    }


}
