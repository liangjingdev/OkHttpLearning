package com.liangjing.httpconnection.services;

/**
 * Created by liangjing on 2017/8/5.
 * function: （多线程处理请求--回调接口）
 * 外层只关心请求结果成功还是失败，所以响应层接口有以下两个方法。
 * （泛型的作用：泛型起到可强制类型转换的作用，通过这个泛型去得到我们想要的结果，
 * 比如说可以得到json数据或者xml数据所对应的实体类）
 * <p>
 * 为什么这里不能够定义成interface而需要定义成一个抽象的类？
 * --因为我们在扩展该网络框架的时候需要去将T这个泛型转化成相对应的实体类对象。
 * 所以这里最好定义成一个抽象类，然后再做一个上层封装--WrapperResponse
 */

public abstract class MultiThreadResponse<T> {

    public abstract void success(MultiThreadRequest request, T data);

    public abstract void fail(int errorCode, String errorMessage);

}
