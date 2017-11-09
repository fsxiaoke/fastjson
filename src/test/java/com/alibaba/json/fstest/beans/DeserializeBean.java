package com.alibaba.json.fstest.beans;

import com.alibaba.fastjson.annotation.JSONField;

public class DeserializeBean {
   public  String value;
   @JSONField(deserialize = false)
    public  int index;

   public DeserializeBean(){}

}
