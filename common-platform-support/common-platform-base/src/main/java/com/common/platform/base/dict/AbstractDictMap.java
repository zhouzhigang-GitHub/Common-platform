package com.common.platform.base.dict;
import java.util.HashMap;
public abstract class AbstractDictMap {
    protected HashMap<String,String>dictry = new HashMap<>();
    protected HashMap<String,String>fieldWarpperDictor = new HashMap<>();
    public AbstractDictMap(){
        put("id","主键id");
        init();
        initBeWrapped();
    }
    //舒适化字段英文名称和中文名称对应的字典
    public abstract void init();
    //初始化需要被包装的自负安（例如：性别为1：男，2：女，需要被包装为汉字）
    protected abstract void initBeWrapped();
    public String get(String key){
        return this.dictry.get(key);
    }
    public void put (String key,String value){
        this.dictry.put(key, value);
    }
    public String getFieldWarpperMethodName(String key){
        return this.fieldWarpperDictor.get(key);
    }
    public void putFieldWrapperMethodName(String key,String methodName){
        this.fieldWarpperDictor.put(key, methodName);
    }
}
