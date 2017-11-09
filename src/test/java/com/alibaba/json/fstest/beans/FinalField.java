package com.alibaba.json.fstest.beans;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class FinalField  {
   public final String value;

    @JSONCreator
    public FinalField(@JSONField(name="value") String value){
        this.value=value;
    }
}
