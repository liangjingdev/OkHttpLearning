package com.liangjing.httpconnection.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liangjing on 2017/8/5.
 * function:测试--实体类
 */

public class Person {

    @SerializedName("username")
    private String name;

    @SerializedName("userage")
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
