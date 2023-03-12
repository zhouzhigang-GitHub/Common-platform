package com.common.platform.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色和菜单关联表
 */

@TableName("sys_relation")
@Getter
@Setter
@ToString
public class Relation implements Serializable {

    private static final long serrialVersionUID= 1L;

    /**
     * 主键
     */
    @TableId(value = "relation_id",type = IdType.ASSIGN_ID)
    private  Long relation;

    /**
     * 菜单id
     */
    @TableField("menu_id")
    private Long menuId;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;
}
