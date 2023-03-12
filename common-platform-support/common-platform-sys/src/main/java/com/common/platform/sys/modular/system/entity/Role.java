package com.common.platform.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.platform.sys.base.pojo.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色表
 */

@TableName("sys_role")
@Getter
@Setter
@ToString
public class Role extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private Long roleId;

    /**
     * 父角色id
     */
    @TableField("pid")
    private Long pid;

    /**
     * 角色名称
     */
    @TableField("name")
    private String name;

    /**
     * 提示
     */
    @TableField("description")
    private String description;

    /**
     * 序号
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 乐观锁
     */
    @TableField("version")
    private Integer version;

}
