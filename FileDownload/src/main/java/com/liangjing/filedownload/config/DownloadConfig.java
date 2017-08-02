package com.liangjing.filedownload.config;

import com.liangjing.filedownload.DownloadManager;

/**
 * Created by liangjing on 2017/8/2.
 * function:Builder模式--1、将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。
 * 2、抽象出对象创建的具体步骤到一个接口，通过调用不同的接口实现，从而得到不同的结果
 * 3、在DownloadManager管理类中，想要提供一些灵活的参数来控制此类中的线程池、执行服务对象创建，例如核心、最大线程数这些参数等。
 * 如此而言就需要在此类中设计多个set方法，而不同参数的组合可能导致set方法需求量的增加，代码冗杂，
 * 所以采用构建者模式来解决这种需求带来的问题。
 */

public class DownloadConfig {

    //核心线程数
    private int coreThreadSize;

    //最大线程数
    private int maxThreadSize;

    //监控进度比例的当地线程数
    private int localProgressThreadSize;

    private DownloadConfig(Builder builder) {
        //用户有可能没有对这些变量进行指定，所以我们需要给他们一个默认值
        coreThreadSize = builder.coreThreadSize == 0 ? DownloadManager.MAX_THREAD : builder.coreThreadSize;
        maxThreadSize = builder.maxThreadSize == 0 ? DownloadManager.MAX_THREAD : builder.coreThreadSize;
        localProgressThreadSize = builder.localProgressThreadSize == 0 ? DownloadManager.LOCAL_PROGRESS_SIZE : builder.localProgressThreadSize;
    }

    public int getCoreThreadSize() {
        return coreThreadSize;
    }

    public int getMaxThreadSize() {
        return maxThreadSize;
    }

    public int getLocalProgressThreadSize() {
        return localProgressThreadSize;
    }

    public static final class Builder {
        private int coreThreadSize;
        private int maxThreadSize;
        private int localProgressThreadSize;

        public Builder() {
        }

        public Builder coreThreadSize(int coreThreadSize) {
            this.coreThreadSize = coreThreadSize;
            return this;
        }

        public Builder maxThreadSize(int maxThreadSize) {
            this.maxThreadSize = maxThreadSize;
            return this;
        }

        public Builder localProgressThreadSize(int localProgressThreadSize) {
            this.localProgressThreadSize = localProgressThreadSize;
            return this;
        }


        public DownloadConfig build() {
            return new DownloadConfig(this);
        }
    }
}
