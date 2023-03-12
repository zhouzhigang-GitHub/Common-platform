package com.common.platform.sys.modular.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.platform.sys.modular.system.entity.Relation;
import com.common.platform.sys.modular.system.mapper.RelationMapper;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表 服务实现类
 */
@Service
public class RelationService extends ServiceImpl<RelationMapper, Relation> {

}
