package com.common.platform.sys.modular.config.model.params;

import com.common.platform.sys.base.pojo.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 参数配置
 */
@Data
public class ConfigParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 属性编码标识
     */
    private String code;

    /**
     * 是否是字典中的值
     */
    private String dictFlag;

    /**
     * 字典类型的编码
     */
    private Long dictTypeId;

    /**
     * 字典的值
     */
    private String dictValue;

    /**
     * 属性值，如果是字典中的类型，则为dict的code
     */
    private String value;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Long updateUser;

    @Override
    public String checkParam() {
        return null;
    }
}
