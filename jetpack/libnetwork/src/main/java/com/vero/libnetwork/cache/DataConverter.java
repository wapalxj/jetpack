package com.vero.libnetwork.cache;

import androidx.room.TypeConverter;

import java.util.Date;

public class DataConverter {


    //存入库时被转化为Long
    @TypeConverter
    public static Long date2Long(Date date) {
        return date.getTime();
    }

    //读取时候被转化为Date
    @TypeConverter
    public static Date long2Date(Long date) {
        return new Date(date);
    }
}
