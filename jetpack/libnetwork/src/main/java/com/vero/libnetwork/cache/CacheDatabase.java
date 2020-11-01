package com.vero.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vero.libcommon.AppGlobals;


@Database(entities = {Cache.class},version = 1,exportSchema = true)
public abstract class CacheDatabase extends RoomDatabase {
    public static CacheDatabase vero_cache;

    static {
        //创建一个内存数据库
        //但是只存在于内存中，进程被杀，数据丢失
//        Room.inMemoryDatabaseBuilder(AppGlobals.getApplication(), CacheDatabase.class);


        vero_cache = Room.databaseBuilder(
                AppGlobals.getApplication(),
                CacheDatabase.class,
                "vero_cache")
                //是否允许在主线程进行查询//默认false
                .allowMainThreadQueries()
//        .addCallback()//数据库创建和打开后的回调
//        .setQueryExecutor()//设置线程池
//        .openHelperFactory()//
//        .setJournalMode()//日志模式
                .fallbackToDestructiveMigration()//升级版本异常的回滚,会删除数据重新创建
//                .addMigrations(sMigration)//数据库升级
                .build();

    }

    public static CacheDatabase get() {
        return vero_cache;
    }

    //获取CacheDao,添加这个方法，编译后会生成CacheDao_impl
    public abstract CacheDao getCache();

//    //数据库从1版本升级到3版本时
//    static Migration sMigration = new Migration(1, 3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("alter table teacher rename to student");
//            database.execSQL("alter table teacher add column teacher_age INTEGER NOT NULL default 0");
//        }
//    };
}
