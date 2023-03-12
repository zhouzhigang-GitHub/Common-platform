package com.common.platform.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录记录
 */

@TableName("sys_login_log")
@Getter
@Setter
@ToString
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "login_log_id", type = IdType.ASSIGN_ID)
    private Long loginLogId;

    /**
     * 日志名称
     */
    @TableField("log_name")
    private String logName;

    /**
     * 管理员id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否执行成功
     */
    @TableField("succeed")
    private String succeed;

    /**
     * 具体消息
     */
    @TableField("message")
    private String message;

    /**
     * 登录ip
     */
    @TableField("ip_address")
    private String ipAddress;

}
