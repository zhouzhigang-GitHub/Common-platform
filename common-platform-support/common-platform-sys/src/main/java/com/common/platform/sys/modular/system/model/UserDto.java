package com.common.platform.sys.modular.system.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户传输 bean
 */
@Data
public class UserDto {
    private Long userId;
    private String account;
    private String password;
    private String name;
    @DateTimeFormat(pattern= "yyyy-MM-dd")
    private Date birthday;
    private String sex;
    private String email;
    private String phone;
    private String roleId;
    private Long deptId;
    private String status;
    private String avatar;

    private String position;

}
