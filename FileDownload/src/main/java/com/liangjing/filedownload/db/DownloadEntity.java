package com.liangjing.filedownload.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liangjing on 2017/8/1.
 * function:单个线程所下载的部分文件数据所相对应的实体类
 */

@Entity
public class DownloadEntity {

    @Id
    private Long id;

    private Long startPosition;
    private Long endPosition;
    private Long progressPosition;
    private String downloadUrl;
    private Integer threadId;
    @Generated(hash = 1903029681)
    public DownloadEntity(Long id, Long startPosition, Long endPosition,
            Long progressPosition, String downloadUrl, Integer threadId) {
        this.id = id;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.progressPosition = progressPosition;
        this.downloadUrl = downloadUrl;
        this.threadId = threadId;
    }
    @Generated(hash = 1671715506)
    public DownloadEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getStartPosition() {
        return this.startPosition;
    }
    public void setStartPosition(Long startPosition) {
        this.startPosition = startPosition;
    }
    public Long getEndPosition() {
        return this.endPosition;
    }
    public void setEndPosition(Long endPosition) {
        this.endPosition = endPosition;
    }
    public Long getProgressPosition() {
        return this.progressPosition;
    }
    public void setProgressPosition(Long progressPosition) {
        this.progressPosition = progressPosition;
    }
    public String getDownloadUrl() {
        return this.downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    public Integer getThreadId() {
        return this.threadId;
    }
    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }
   
}
