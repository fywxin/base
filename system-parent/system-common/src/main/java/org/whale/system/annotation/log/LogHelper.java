package org.whale.system.annotation.log;

import com.alibaba.fastjson.JSON;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.common.util.ThreadContext;

/**
 * 日志模板参数赋值工具类
 *
 * Created by 王金绍 on 2016/4/25.
 */
public class LogHelper {

    /**
     * 设置参数值
     * @param args
     */
    public static void addPlaceHolder(Object... args){
        if (args != null){
            int i=0;
            String[] params = new String[args.length];
            for (Object obj : args){
                if (obj == null){
                    params[i] = null;
                }else if (ReflectionUtil.isBaseDataType(obj.getClass())){
                    params[i] = obj.toString();
                }else{
                    params[i] = JSON.toJSONString(obj);
                }
                i++;
            }
            ThreadContext.getContext().put(ThreadContext.KEY_LOG_DATAS, params);
        }
    }

    /**
     * 设置参数值
     * @param args
     */
    public static void addPlaceHolder(String... args){
        if (args != null){
            ThreadContext.getContext().put(ThreadContext.KEY_LOG_DATAS, args);
        }
    }
}
