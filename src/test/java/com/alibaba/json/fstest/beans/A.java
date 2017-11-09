package com.alibaba.json.fstest.beans;

import com.alibaba.fastjson.annotation.JSONCreator;

public class A {
    public A(){

    }
    @JSONCreator
    public A(int m){
        this.m=true;
    }

    public boolean isM() {
        return m;
    }

    boolean m;

}
