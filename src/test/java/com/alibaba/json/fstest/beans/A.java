package com.alibaba.json.fstest.beans;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class A {
    public A(){

    }
    @JSONCreator
    public A(@JSONField(name="m") int m){
        this.m=true;
    }

    public boolean isM() {
        return m;
    }

    boolean m;

}
