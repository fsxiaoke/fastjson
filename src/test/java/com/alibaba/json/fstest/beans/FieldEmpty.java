package com.alibaba.json.fstest.beans;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class FieldEmpty {
    int age;
    String name;
    A a;
    @JSONCreator
    public FieldEmpty(@JSONField(name="name") String name, @JSONField(name="age") int age, @JSONField(name="a")A a){
        this.name=name;
        this.age=age;
        this.a=a;
    }




}
