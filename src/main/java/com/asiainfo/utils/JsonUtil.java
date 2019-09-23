package com.asiainfo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @Classname JsonUtil
 * @Description TODO
 * @Date 2019/9/16 9:54
 * @Created by Jhon_yh
 */
@Component
public class JsonUtil {
    public JSONObject str2JsonObj(String str) {
        return JSON.parseObject(str);
    }

    public String toJsonString(Object o) {
        return JSON.toJSONString(o);
    }

}
