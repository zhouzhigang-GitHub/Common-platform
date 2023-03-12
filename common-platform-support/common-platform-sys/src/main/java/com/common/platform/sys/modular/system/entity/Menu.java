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
 * 菜单表
 */
@TableName("sys_menu")
@Setter
@Getter
@ToString
public class Menu extends BaseEntity implements Serializable {

    private static final long serialVersionUID= 1L;

    /**
     * 主键id
     */
    @TableId(value = "menu_id",type= IdType.ASSIGN_ID)
    private Long menuId;

    /**
     * 菜单编号
     */
    @TableField("code")
    private String code;

    /**
     * 菜单父编号
     */
    @TableField("pcode")
    private String pcode;

    /**
     * 当前菜单的所有父菜单编号
     */
    @TableField("pcodes")
    private String pcodes;

    /**
     * 菜单名称
     */
    @TableField("name")
    private String name;

    /**
     * 菜单图标
     */
    @TableField("icon")
    private String icon;

    /**
     * url地址
     */
    @TableField("url")
    private String url;

    /**
     * 菜单排序号
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 菜单层级
     */
    @TableField("levels")
    private Integer levels;

    /**
     * 是否是菜单（字典）
     */
    @TableField("menu_flag")
    private String menuFlag;

    /**
     * 备注
     */
    @TableField("description")
    private String description;

    /**
     * 菜单状态（字典）
     */
    @TableField("status")
    private String status;

    /**
     * 是否打开新页面的标识（字典）
     */
    @TableField("new_page_flag")
    private String newPageFlag;

    /**
     * 是否打开（字典）
     */
    @TableField("open_flag")
    private String openFlag;

    /**
     * 系统分类（字典）
     */
    @TableField("system_type")
    private String systemType;
}
