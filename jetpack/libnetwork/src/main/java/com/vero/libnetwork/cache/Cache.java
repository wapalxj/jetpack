package com.vero.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

/**
 * 表
 */
@Entity(tableName = "cache"
//        ,
//        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "key",
//                onDelete = ForeignKey.RESTRICT, onUpdate = ForeignKey.SET_DEFAULT
//
//        )},
//        indices = {@Index(value = {"key", "id"})}//加快查询操作，减慢插入和更新操作


)
public class Cache implements Serializable {
    @NonNull
    @PrimaryKey
    public String key;

    @ColumnInfo(name = "_data")//用此注解更改数据库列名字
    public byte[] data;

    //parentColumn指当前表的列名
    //entityColumn指User表的列名
    //从User表中查询与Cache表中id相等的User对象赋值给mUser
//    @Relation(entity = User.class,parentColumn = "id",entityColumn = "id",projection = {})
//    public User mUser;


//    @TypeConverters(value = {DataConverter.class})
//    public Date mDate;


}
