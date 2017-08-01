package com.liangjing.filedownload;

import com.liangjing.filedownload.http.DownloadCallback;

/**
 * Created by liangjing on 2017/8/1.
 * function:下载任务--引入队列机制
 * 一个DownloadTask对应着一个下载任务，然后由一个HashSet<>()集合统一对这些下载任务进行管理，可避免重复提交等等
 * 然后每个下载任务都是利用多线程进行下载，多线程下载当中又利用到线程池，并引入队列机制，对每个线程进行管理
 */

public class DownloadTask {

    private String mUrl;

    private DownloadCallback mCallback;

    public DownloadTask(String mUrl, DownloadCallback mCallback) {
        this.mCallback = mCallback;
        this.mUrl = mUrl;
    }

    public DownloadCallback getCallback() {
        return mCallback;
    }

    public void setCallback(DownloadCallback mCallback) {
        this.mCallback = mCallback;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadTask that = (DownloadTask) o;

        if (mUrl != null ? !mUrl.equals(that.mUrl) : that.mUrl != null) return false;
        return mCallback != null ? mCallback.equals(that.mCallback) : that.mCallback == null;

    }

    @Override
    public int hashCode() {
        int result = mUrl != null ? mUrl.hashCode() : 0;
        result = 31 * result + (mCallback != null ? mCallback.hashCode() : 0);
        return result;
    }
}
