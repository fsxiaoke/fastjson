package com.alibaba.json.fstest;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.fstest.beans.A;
import junit.framework.TestCase;
import org.junit.Assert;

public class JSON_GenericsTest extends TestCase {

    public void test_create() throws Exception {

    }
    
    public void test_create_2() throws Exception {
        A a=JSON.parseObject("{\"m\":0}",A.class);
        Assert.assertTrue(a.isM());
    }

}
