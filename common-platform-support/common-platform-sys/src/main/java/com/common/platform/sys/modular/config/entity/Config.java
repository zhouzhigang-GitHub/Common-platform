package com.common.platform.sys.modular.config.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.common.platform.sys.base.pojo.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 参数配置
 */

@TableName("sys_config")
@Getter
@Setter
@ToString
public class Config extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 属性编码标识
     */
    @TableField("code")
    private String code;

    /**
     * 是否是字典中的值
     */
    @TableField("dict_flag")
    private String dictFlag;

    /**
     * 字典类型的编码
     */
    @TableField("dict_type_id")
    private Long dictTypeId;

    /**
     * 属性值，如果是字典中的类型，则为dict的code
     */
    @TableField("value")
    private String value;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
