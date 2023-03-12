package com.common.platform.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 */

@TableName("sys_operation_log")
@Setter
@Getter
@ToString
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "operation_log_id", type = IdType.ASSIGN_ID)
    private Long operationLogId;

    /**
     * 日志类型(字典)
     */
    @TableField("log_type")
    private String logType;

    /**
     * 日志名称
     */
    @TableField("log_name")
    private String logName;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 类名称
     */
    @TableField("class_name")
    private String className;

    /**
     * 方法名称
     */
    @TableField("method")
    private String method;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否成功(字典)
     */
    @TableField("succeed")
    private String succeed;

    /**
     * 备注
     */
    @TableField("message")
    private String message;

}
