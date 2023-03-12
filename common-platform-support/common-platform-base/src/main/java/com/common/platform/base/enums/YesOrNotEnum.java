package com.common.platform.base.enums;
import lombok.Getter;
@Getter
public enum  YesOrNotEnum {
    Y(true,"是",1),
    N(false,"否",0);
    private Boolean flag;
    private String desc;
    private Integer code;
    YesOrNotEnum(Boolean flag,String desc,Integer code){
        this.flag = flag;
        this.code = code;
        this.desc = desc;
    }
    public static String valueOf(Integer ststus){
        if (ststus==null){
            return "";
        }else {
            for (YesOrNotEnum s:
                 YesOrNotEnum.values()) {
                if (s.getCode().equals(ststus)){
                    return s.getDesc();
                }
            }
        }
        return "";
    }

}
