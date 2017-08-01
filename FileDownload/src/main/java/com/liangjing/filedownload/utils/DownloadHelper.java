package com.liangjing.filedownload.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.liangjing.filedownload.db.DaoMaster;
import com.liangjing.filedownload.db.DaoSession;
import com.liangjing.filedownload.db.DownloadEntity;
import com.liangjing.filedownload.db.DownloadEntityDao;

import java.util.List;

/**
 * Created by liangjing on 2017/8/1.
 * <p>
 * function:用来处理多线程文件下载进度数据保存之类的问题(如将数据存储到我们的数据库当中) 添加、查询数据
 * 目的：在恢复下载的时候，可以直接从数据库当中取出当前线程所下载的进度(数据)，然后可以从当前线程还没有下载的部分开始
 * 去下载数据。达到断点续传的目的
 */

public class DownloadHelper {

    private static DownloadHelper sHelper = new DownloadHelper();
    private DaoMaster mMaster;
    private DaoSession mDaoSession;
    private DownloadEntityDao mDao;

    private DownloadHelper() {
    }

    public static DownloadHelper getInstance(){
        return sHelper;
    }

    /**
     * function:初始化操作
     * @param context
     */
    public void init(Context context){

        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context,"download.db",null).getWritableDatabase();
        mMaster = new DaoMaster(db);
        mDaoSession = mMaster.newSession();
        mDao = mDaoSession.getDownloadEntityDao();
    }

    /**
     * function:将实体类插入到数据库当中
     * @param entity
     */
    public void insert(DownloadEntity entity){
        mDao.insertOrReplace(entity);
    }

    /**
     * 根据给定的url来查询相关的数据
     * @param url
     * @return
     */
    public List<DownloadEntity> getAll(String url){

        return mDao.queryBuilder().where(DownloadEntityDao.Properties.DownloadUrl.eq(url)).orderAsc(DownloadEntityDao.Properties.ThreadId).list();
    }


}
