package com.example.webstarter.Interceptor;

import java.util.Map;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 20:04
 * @注释 请求上下文管理器
 */
public class LiveRequestContext {
    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocal<>();

    //创建一个set方法，用来向线程中存入数据
    public static void set(Object key, Object value){
        if(key==null){
            throw new IllegalArgumentException("key不能为空");
        }
        if(value==null){
            resources.get().remove(key);
        }
        resources.get().put(key, value);
    }

    //创建一个get方法，用来取出线程中的数据
    public static Object get(Object key){
        if(key==null){
            throw new IllegalArgumentException("key不能为空");
        }
        return resources.get().get(key);
    }

    //创建一个方法直接获取到用户id
    public static Long getUserID(){
        Object ID = LiveRequestContext.get(RequestConstants.LIVE_USER_ID);
        if(ID==null){
            return null;
        }
        return (Long) ID;
    }

    //创建一个clear方法，用来清空线程中的数据，防止内存泄漏
    public static void clear(){
        resources.remove();
    }


    //TODO 和InheritableThreadLocal
}
