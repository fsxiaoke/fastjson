package com.alibaba.json.fstest.beans;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class Child extends Parent{
    public int age;

    @JSONCreator
    public Child(@JSONField(name="age") int age){
        this.age=age;
    }
}
