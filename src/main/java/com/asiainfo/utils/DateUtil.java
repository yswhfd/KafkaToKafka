package com.asiainfo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname DateUtil
 * @Description TODO
 * @Date 2019/9/16 16:39
 * @Created by Jhon_yh
 */
public class DateUtil {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     *
     * @param date Date 类型
     * @return 2010-10-10 10:10:10
     */
    public String getFormatedTime(Date date) {
        return sdf.format(date);
    }

    public String getTimeStamp(){
        return String.valueOf(System.currentTimeMillis());
    }



}
